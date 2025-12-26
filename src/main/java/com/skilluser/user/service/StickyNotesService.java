package com.skilluser.user.service;

import com.skilluser.user.model.StickyNotes;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface StickyNotesService
{
    StickyNotes addStickyNotes(StickyNotes stickyNotes, Long userId);
    StickyNotes updateStickyNotes(StickyNotes stickyNotes, Long noteId);
    Map<String,Object> getAllNotesByUserId(Long userId, int page, int size,
                                           String sortBy, String direction);
    void deleteNotesById(Long noteId);

}
