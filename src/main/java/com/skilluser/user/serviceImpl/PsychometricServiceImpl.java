package com.skilluser.user.serviceImpl;

import com.skilluser.user.dto.*;
import com.skilluser.user.enums.QuestionType;
import com.skilluser.user.enums.TestSection;
import com.skilluser.user.model.User;
import com.skilluser.user.model.psychomatrictest.*;
import com.skilluser.user.repository.*;
import com.skilluser.user.service.PsychometricService;
import com.skilluser.user.utility.PaginationUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
                    psychometricQuestionRepository.findByTestIdAndSection(test.getId(), section);

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
    public Map<String, Object> submitTest(Long attemptId, Long userId, List<AnswerDto> answers) {
        PsychometricAttempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() -> new RuntimeException("Attempt not found"));

        //  Prevent double submission
        if (attempt.isSubmitted()) {
            throw new RuntimeException("Test already submitted");
        }

        // Clean safety: remove any existing answers for this attempt
        // (important if frontend retries)

        for (AnswerDto a : answers) {

            PsychometricQuestion question =
                    psychometricQuestionRepository.findById(a.getQuestionId())
                            .orElseThrow(() -> new RuntimeException("Question not found"));

            PsychometricAnswer answer = new PsychometricAnswer();
            answer.setAttempt(attempt);
            answer.setQuestion(question);
            answer.setSelectedAnswer(a.getSelectedAnswer());
            answer.setDescriptiveAnswer(a.getDescriptiveAnswer());

            answerRepository.save(answer);
        }

        attempt.setSubmitted(true);
        attemptRepository.save(attempt);

        List<PsychometricAnswer> ans =
                answerRepository.findByAttemptId(attempt.getId());

        if (answers.isEmpty()) {
            throw new RuntimeException("No answers submitted");
        }

        // CALL LLM (ONLY Q + A)
       //   String aiResult = llmService.analyze(attempt.getUserId(), answers);

        // SAVE AI RESULT
//        PsychometricResult result = new PsychometricResult();
//        result.setAttempt(attempt);
//        result.setAiSummary(aiResult);

       // resultRepo.save(result);
       User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User Not Found"+userId));

        Map<String,Object> map = new HashMap<>();
        map.put("answers",ans);
        map.put("userId",userId);
        map.put("username",user.getName());
        return map;
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
