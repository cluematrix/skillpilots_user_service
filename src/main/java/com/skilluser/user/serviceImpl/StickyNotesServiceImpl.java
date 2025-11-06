package com.skilluser.user.serviceImpl;

import com.skilluser.user.model.StickyNotes;
import com.skilluser.user.repository.StickyNotesRepository;
import com.skilluser.user.repository.UserRepository;
import com.skilluser.user.service.StickyNotesService;
import com.skilluser.user.utility.PaginationUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class StickyNotesServiceImpl implements StickyNotesService
{
    private final StickyNotesRepository stickyNotesRepository;
    private final UserRepository userRepository;
    @Override
    public StickyNotes addStickyNotes(StickyNotes stickyNotes, Long userId)
    {
        stickyNotes.setUserId(userId);
        stickyNotes.setDate(new java.util.Date());
        return stickyNotesRepository.save(stickyNotes);
    }

    @Override
    public StickyNotes updateStickyNotes(StickyNotes stickyNotes, Long noteId)
    {
        StickyNotes existingNote = stickyNotesRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Notes Not found :" + noteId));

        existingNote.setNotes(stickyNotes.getNotes());
        existingNote.setDate(new java.util.Date());
        return stickyNotesRepository.save(existingNote);
    }

    @Override
    public Map<String,Object> getAllNotesByUserId(Long userId,int page,int size,
                                                  String sortBy,String direction)
    {
        Pageable pageable = PaginationUtil.createPageRequest(page, size, sortBy, direction);
        Page<StickyNotes> notesPage = stickyNotesRepository.findByUserId(userId, pageable);
        return PaginationUtil.buildResponse(notesPage);
    }

    @Override
    public void deleteNotesById(Long noteId)
    {
        StickyNotes existingNotes = stickyNotesRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Notes not found :" + noteId));
        stickyNotesRepository.delete(existingNotes);
    }
}
