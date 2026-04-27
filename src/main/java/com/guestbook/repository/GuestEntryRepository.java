package com.guestbook.repository;

import com.guestbook.model.GuestEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface GuestEntryRepository extends JpaRepository<GuestEntry, Long> {

    Page<GuestEntry> findAllByOrderByPinnedDescCreatedAtDesc(Pageable pageable);

    @Query("SELECT e FROM GuestEntry e WHERE " +
           "LOWER(e.name) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(e.message) LIKE LOWER(CONCAT('%', :q, '%')) " +
           "ORDER BY e.pinned DESC, e.createdAt DESC")
    Page<GuestEntry> search(@Param("q") String query, Pageable pageable);

    long countByCreatedAtAfter(LocalDateTime dateTime);

    @Query("SELECT COALESCE(SUM(e.likes), 0) FROM GuestEntry e")
    long sumAllLikes();
}
