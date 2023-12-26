package com.dailymuse.microservicenote.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository
        extends JpaRepository<Note, Long> {
    @Query("SELECT n FROM Note n WHERE YEAR(n.date) = :year AND MONTH(n.date) = :month AND n.userId = :userId")
    List<Note> findAllByUserIdAndMonth(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT n FROM Note n WHERE YEAR(n.date) = :year AND MONTH(n.date) = :month " +
            "AND DAY(n.date) = :day AND n.userId = :userId")
    Note findByUserIdAndDay(@Param("userId") Long userId, @Param("year") int year,
                                  @Param("month") int month, @Param("day") int day);
}

