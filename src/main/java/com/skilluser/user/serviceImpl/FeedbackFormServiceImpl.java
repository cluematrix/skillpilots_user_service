package com.skilluser.user.serviceImpl;

import com.skilluser.user.model.FeedbackForm;
import com.skilluser.user.repository.FeedbackFormRepository;
import com.skilluser.user.service.FeedbackFormService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FeedbackFormServiceImpl implements FeedbackFormService {
    @Autowired
    private FeedbackFormRepository feedbackFormRepository;


    // create feedback form
    @Override
    @Transactional
    public FeedbackForm createFeedbackForm(FeedbackForm feedbackForm) {
        feedbackForm.setCreatedDate(LocalDate.now());
        return feedbackFormRepository.save(feedbackForm);
    }


    // get feedback form by Id
    @Override
    public FeedbackForm getFeedbackFormById(Long id) {
        FeedbackForm feedbackForm = feedbackFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback form is not found for this id: "+id));
        return feedbackForm;
    }


    // get all feedback forms
    @Override
    public List<FeedbackForm> getAllFeedbackForms() {
        return feedbackFormRepository.findAll();
    }


    // delete By id
    @Override
    public void deleteById(Long id) {
        feedbackFormRepository.deleteById(id);
    }


    // update feedback form
    @Override
    public FeedbackForm updateFeedbackForm(FeedbackForm feedbackForm, Long id) {
        // find feedback form by id
        FeedbackForm form = feedbackFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback form is not found for this id: "+id));

        form.setEmail(feedbackForm.getEmail());
        form.setFullName(feedbackForm.getFullName());
        form.setCollegeName(feedbackForm.getCollegeName());
        form.setCourse(feedbackForm.getCourse());
        form.setExampleExplanation(feedbackForm.getExampleExplanation());
        form.setCareerGrowthAndDirection(feedbackForm.getCareerGrowthAndDirection());
        form.setCurrentYearOfStudy(feedbackForm.getCurrentYearOfStudy());
        form.setFreeTrialOfPsychoTest(feedbackForm.getFreeTrialOfPsychoTest());
        form.setImproveFutureSession(feedbackForm.getImproveFutureSession());
        form.setMobileNumber(feedbackForm.getMobileNumber());
        form.setTouchInFutureForProgram(feedbackForm.getTouchInFutureForProgram());
        form.setOverallSession(feedbackForm.getOverallSession());
        form.setSessionLikeInFuture(feedbackForm.getSessionLikeInFuture());
        form.setUpdatedDate(LocalDate.now());
        form.setStudentFriendly(feedbackForm.getStudentFriendly());
        form.setAnyDoubts(feedbackForm.getAnyDoubts());

        return feedbackFormRepository.save(form);

    }




}
