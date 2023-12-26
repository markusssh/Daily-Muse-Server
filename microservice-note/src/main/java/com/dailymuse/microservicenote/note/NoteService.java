package com.dailymuse.microservicenote.note;

import com.dailymuse.microservicenote.config.exception.NoteAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public List<Note> getUserNotesInMonth(Long userId, LocalDate currentDate) {
        return noteRepository
                .findAllByUserIdAndMonth(
                        userId,
                        currentDate.getYear(),
                        currentDate.getMonthValue()
                );
    }

    public Note findNote(Long userId, LocalDate date) {
        return noteRepository.findByUserIdAndDay(
                userId,
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth()
        );
    }

    public void addNote(
            NoteRequest request,
            Long userId
    ) {
        Note note = findNote
                (
                        userId,
                        request.getDate()
                );
        if (note != null) throw
                new NoteAlreadyExistsException
                        (
                                "Note of the date " + request.getDate() + " does already exist"
                        );
        else {
            Note noteSave = Note
                    .builder()
                    .userId(userId)
                    .content(request.getContent())
                    .mood(request.getMood())
                    .date(request.getDate())
                    .build();
            noteRepository.save(noteSave);
        }
    }

    public void updateNote(
            NoteRequest request,
            Long userId
    ) {
        Note note = findNote
                (
                        userId,
                        request.getDate()
                );
        if (note == null) throw
                new NoteAlreadyExistsException
                        (
                                "Note of the date " + request.getDate() + " does not exist"
                        );
        else {
            Note noteSave = Note
                    .builder()
                    .id(note.getId())
                    .userId(userId)
                    .content(request.getContent())
                    .mood(request.getMood())
                    .date(request.getDate())
                    .build();
            noteRepository.save(noteSave);
        }
    }

}
