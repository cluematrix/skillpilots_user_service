package com.skilluser.user.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilluser.user.dto.*;
import com.skilluser.user.dto.ai.AiAnalysisPayload;
import com.skilluser.user.dto.ai.AiAnswerPayload;
import com.skilluser.user.dto.ai.AttemptPayload;
import com.skilluser.user.dto.ai.QuestionPayload;
import com.skilluser.user.enums.QuestionType;
import com.skilluser.user.enums.TestSection;
import com.skilluser.user.model.User;
import com.skilluser.user.model.psychomatrictest.*;
import com.skilluser.user.repository.*;
import com.skilluser.user.service.PsychometricService;
import com.skilluser.user.utility.AiClient;
import com.skilluser.user.utility.PaginationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PsychometricServiceImpl implements PsychometricService {

    private final PsychometricTestRepository psychometricTestRepository;
    private final PsychometricQuestionRepository psychometricQuestionRepository;
    private final PsychometricOptionRepository optionRepository;
    private final PsychometricAttemptRepository attemptRepository;
    private final PsychometricAnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final AiClient aiClient;
    private final PsychometricResultRepository psychometricResultRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PsychometricTest createTest(PsychometricTest psychometricTest) {
        return psychometricTestRepository.save(psychometricTest);
    }

    @Override
    public Map<String, Object> getTests(Pageable pageable) {
        Page<PsychometricTest> testPage = psychometricTestRepository.findAll(pageable);
        return PaginationUtil.buildResponse(testPage);
    }

    @Transactional
    @Override
    public void addQuestions(AddQuestionsRequest req) {

        PsychometricTest test = psychometricTestRepository.findById(req.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        for (QuestionCreateDto qReq : req.getQuestions()) {

            PsychometricQuestion q = new PsychometricQuestion();
            q.setTest(test);
            q.setSection(qReq.getSection());
            q.setType(qReq.getType());
            q.setQuestionText(qReq.getQuestionText());

            q = psychometricQuestionRepository.save(q);

            // MCQ OPTIONS
            if (qReq.getType() == QuestionType.MCQ) {

                if (qReq.getOptions() == null || qReq.getOptions().isEmpty()) {
                    throw new RuntimeException("MCQ question must have options");
                }

                for (OptionDto o : qReq.getOptions()) {
                    PsychometricOption opt = new PsychometricOption();
                    opt.setQuestion(q);
                    opt.setLabel(o.getLabel());
                    opt.setOptionText(o.getText());
                    optionRepository.save(opt);
                }
            }
        }
    }

    @Override
    public StartTestResponse startTest(StartTestRequest req) {

        PsychometricTest test =
                psychometricTestRepository.findById(req.getTestId()).orElseThrow();

        PsychometricAttempt attempt = new PsychometricAttempt();
        attempt.setUserId(req.getUserId());
        attempt.setTest(test);
        attempt.setSubmitted(false);

        attempt = attemptRepository.save(attempt);

        Map<TestSection, List<QuestionDto>> sectionMap = new LinkedHashMap<>();

        for (TestSection section : TestSection.values()) {

            List<PsychometricQuestion> questions =
                    psychometricQuestionRepository.findByTestIdAndSectionAndIsDeleteFalse(test.getId(), section);

            Collections.shuffle(questions);

            List<QuestionDto> qDtos = questions.stream()
                    .map(this::mapToDto)
                    .toList();

            sectionMap.put(section, qDtos);
        }

        return new StartTestResponse(
                attempt.getId(),
                test.getName(),
                sectionMap
        );
    }

    @Override
    @Transactional
    public Map<String, Object> submitTest(
            Long attemptId,
            Long userId,
            List<AnswerDto> answers) {

        // Attempt
        PsychometricAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));

        if (attempt.isSubmitted()) {
            throw new RuntimeException("Test already submitted");
        }

        // Save answers
        for (AnswerDto a : answers) {

            PsychometricQuestion question =
                    psychometricQuestionRepository.findById(a.getQuestionId())
                            .orElseThrow(() -> new RuntimeException("Question not found"));

            PsychometricAnswer ans = new PsychometricAnswer();
            ans.setAttempt(attempt);
            ans.setQuestion(question);
            ans.setSelectedAnswer(a.getSelectedAnswer());
            ans.setDescriptiveAnswer(a.getDescriptiveAnswer());

            answerRepository.save(ans);
        }

        // mark submitted
        attempt.setSubmitted(true);
        attemptRepository.save(attempt);

        //  Fetch saved answers
        List<PsychometricAnswer> savedAnswers =
                answerRepository.findByAttemptId(attempt.getId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //  BUILD ATTEMPT PAYLOAD ONCE (NEVER NULL)
        AttemptPayload attemptPayload = new AttemptPayload();
        attemptPayload.setId(attempt.getId());
        attemptPayload.setUserId(user.getId());
        attemptPayload.setSubmitted(attempt.isSubmitted());
        attemptPayload.setStartedAt(
                attempt.getStartedAt()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        //  BUILD AI ANSWERS
        List<AiAnswerPayload> aiAnswers = new ArrayList<>();

        for (PsychometricAnswer a : savedAnswers) {

            AiAnswerPayload p = new AiAnswerPayload();
            p.setId(a.getId());
            p.setAttempt(attemptPayload);

            QuestionPayload q = new QuestionPayload();
            q.setId(String.valueOf(a.getQuestion().getId()));
            q.setSection(a.getQuestion().getSection().name());
            q.setType(a.getQuestion().getType().name());
            q.setQuestionText(a.getQuestion().getQuestionText());

            p.setQuestion(q);
            p.setSelectedAnswer(a.getSelectedAnswer());
            p.setDescriptiveAnswer(a.getDescriptiveAnswer());

            aiAnswers.add(p);
        }

        AiAnalysisPayload aiPayload = new AiAnalysisPayload();
        aiPayload.setAnswers(aiAnswers);
        Object aiResponseObject = aiClient.callAiAnalysis(aiPayload);

        String aiSummaryJson;

        try {
            aiSummaryJson = objectMapper.writeValueAsString(aiResponseObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert AI response to JSON", e);
        }
      //  Object summary = aiClient.callAiAnalysis(aiPayload);

        PsychometricResult result = new PsychometricResult();
        result.setAttempt(attempt);
        result.setAiSummary(aiSummaryJson);
        result.setUserId(userId);
        psychometricResultRepository.save(result);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("username", user.getName());
        map.put("summary", aiSummaryJson);

        return map;
    }

    @Override
    public List<Map<String, Object>> getSummary(Long userId) throws JsonProcessingException {

        List<PsychometricResult> results =
                psychometricResultRepository.findByUserIdOrderByGeneratedAtDesc(userId);

        List<Map<String, Object>> summaries = new ArrayList<>();

        for (PsychometricResult r : results) {
            summaries.add(
                    objectMapper.readValue(r.getAiSummary(), Map.class)
            );
        }

        return summaries;
    }


    private QuestionDto mapToDto(PsychometricQuestion q) {

        List<OptionDto> options = new ArrayList<>();

        if (q.getType() == QuestionType.MCQ) {
            options = optionRepository.findByQuestionId(q.getId())
                    .stream()
                    .map(o -> new OptionDto(o.getLabel(), o.getOptionText()))
                    .toList();
        }

        return new QuestionDto(
                q.getId(),
                q.getQuestionText(),
                q.getType(),
                options
        );
    }


}
