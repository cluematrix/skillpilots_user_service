package com.skilluser.user.service;

import com.skilluser.user.model.StickyNotes;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface StickyNotesService
{
    public StickyNotes addStickyNotes(StickyNotes stickyNotes,Long userId);
    public StickyNotes updateStickyNotes(StickyNotes stickyNotes,Long noteId);
    public Map<String,Object> getAllNotesByUserId(Long userId,int page,int size,
                                                  String sortBy,String direction);
    public void deleteNotesById(Long noteId);

}
