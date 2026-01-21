package com.skilluser.user.controller;

import com.skilluser.user.model.FeedbackForm;
import com.skilluser.user.service.FeedbackFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users/feedbackForm")
public class FeedbackFormController {
    @Autowired
    private FeedbackFormService feedbackFormService;


    // create feedback form
    @PostMapping
    public ResponseEntity<Map<String,Object>> createFeedbackForm(@RequestBody FeedbackForm feedbackForm){
        Map<String, Object> response = new HashMap<>();
        try {
            FeedbackForm form = feedbackFormService.createFeedbackForm(feedbackForm);
            response.put("status", "success");
            response.put("data", form);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e){
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("errorType", e.getClass().getSimpleName());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    // get feedback form by Id
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackForm> getFeedbackFormById(@PathVariable  Long id){
        FeedbackForm form = feedbackFormService.getFeedbackFormById(id);
        return ResponseEntity.ok(form);
    }


    // get all Feedback forms
    @GetMapping
    public ResponseEntity<List<FeedbackForm>> getAllFeedbackForms(){
        List<FeedbackForm> forms = feedbackFormService.getAllFeedbackForms();
        return ResponseEntity.ok(forms);
    }


    // delete feedback form by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteFeedbackFormById(@PathVariable Long id){
        Map<String,String> response = new HashMap<>();
        try{
            feedbackFormService.deleteById(id);
            response.put("message","Feedback form is deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message",e.getMessage());
            return ResponseEntity.ok(response);
        }
    }



    // update Feedback form
    @PutMapping("/{id}")
    public ResponseEntity<Map<String,String>> updateFeedbackForm(@RequestBody FeedbackForm feedbackForm,
                                                                 @PathVariable Long id)
    {
        Map<String,String> response = new HashMap<>();
        try{
            feedbackFormService.updateFeedbackForm(feedbackForm,id);
            response.put("message","Feedback form is updated successfully.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message",e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
