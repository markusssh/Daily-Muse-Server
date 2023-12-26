package com.dailymuse.microservicenote.note;

import com.dailymuse.microservicenote.config.exception.ExceptionMessageHandler;
import com.dailymuse.microservicenote.config.exception.NoteAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/note")
public class NoteController {

    private final NoteService noteService;

    @GetMapping(path = "month")
    public ResponseEntity<?> getUserNotes
            (
                    @RequestHeader("x-user-id") Long userId,
                    @RequestParam LocalDate current_date
            ) {
        NoteResponse response = NoteResponse
                .builder()
                .notes(noteService.getUserNotesInMonth(userId, current_date))
                .build();
        if (response.getNotes().isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
        else return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> addNote
            (
                    @RequestHeader("x-user-id") Long userId,
                    @RequestBody NoteRequest request
            ) {
        try {
            noteService.addNote(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Note has been created");
        }
        catch (NoteAlreadyExistsException ex) {
            String errorMessage = ExceptionMessageHandler.NOTE_ALREADY_EXISTS;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateNote
            (
                    @RequestHeader("x-user-id") Long userId,
                    @RequestBody NoteRequest request
            ) {
        try {
            noteService.updateNote(request, userId);
            return ResponseEntity.status(HttpStatus.OK).body("Note has been updated");
        }
        catch (NoteAlreadyExistsException ex) {
            String errorMessage = ExceptionMessageHandler.NOTE_DOES_NOT_EXIST;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
}
