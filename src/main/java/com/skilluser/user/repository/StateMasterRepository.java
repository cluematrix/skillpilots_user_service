package com.skilluser.user.repository;

import com.skilluser.user.model.StateMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StateMasterRepository extends JpaRepository<StateMaster,Long> {

    Optional<StateMaster> findByStateNameIgnoreCase(String stateName);

}
