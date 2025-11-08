package com.skilluser.user.controller;

import com.skilluser.user.model.StickyNotes;
import com.skilluser.user.service.StickyNotesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/users/notes")
public class StickyNotesController
{
    private final StickyNotesService stickyNotesService;

    // Stick Notes Implementations - Ajay - 5-11-2025
    @PostMapping("/{userId}")
    public ResponseEntity<?> saveStickyNotes(@RequestBody StickyNotes stickyNotes,
                                             @PathVariable Long userId)
    {
        StickyNotes notes = stickyNotesService.addStickyNotes(stickyNotes, userId);
        return ResponseEntity.ok(Map.of(
                "message","Note Added Successfully!",
                "Data" ,notes
        ));
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<?> updateStickyNotes(@PathVariable Long noteId,
                                               @RequestBody StickyNotes stickyNotes)
    {
        StickyNotes updatedNote = stickyNotesService.updateStickyNotes(stickyNotes, noteId);
        if (updatedNote == null)
        {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Note not found",
                    "noteId", noteId
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message","Note Updated Successfully!",
                "Note Id", noteId,
                "data",updatedNote
        ));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAllNotesByUser(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "date") String sortBy,
                                               @RequestParam(defaultValue = "desc") String direction)
    {
        Map<String, Object> allNotesByUser = stickyNotesService.getAllNotesByUserId(userId, page, size, sortBy, direction);
        return ResponseEntity.ok(Map.of(
                "message","Notes fetched Successfully!",
                "user id",userId,
                "data",allNotesByUser
        ));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> deleteNotesById(@PathVariable Long noteId)
    {
        stickyNotesService.deleteNotesById(noteId);
        return ResponseEntity.ok(Map.of(
                "message","Note Deleted Successfully!",
                "NoteId",noteId));
    }
}
