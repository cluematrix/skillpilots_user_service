package com.skilluser.user.service;

import com.skilluser.user.model.FeedbackForm;

import java.util.List;

public interface FeedbackFormService {

    // create feedback form
    FeedbackForm createFeedbackForm(FeedbackForm feedbackForm);

    // get feedback form by Id
    FeedbackForm getFeedbackFormById(Long id);

    // get all feedback forms
    List<FeedbackForm> getAllFeedbackForms();

    // delete feedback form by Id
    void deleteById(Long id);

    // update feedback form
    FeedbackForm updateFeedbackForm(FeedbackForm feedbackForm, Long id);
}
