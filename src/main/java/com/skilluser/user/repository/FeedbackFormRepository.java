package com.skilluser.user.repository;

import com.skilluser.user.model.FeedbackForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackFormRepository extends JpaRepository<FeedbackForm,Long> {
}
