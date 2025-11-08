package com.skilluser.user.repository;

import com.skilluser.user.model.StickyNotes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StickyNotesRepository extends JpaRepository<StickyNotes,Long> {
    Page<StickyNotes> findByUserId(Long userId, Pageable pageable);
}
