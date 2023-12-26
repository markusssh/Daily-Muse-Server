package com.dailymuse.microservicenote.note;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequest {

    private String content;
    private Byte mood;
    private LocalDate date;
}
