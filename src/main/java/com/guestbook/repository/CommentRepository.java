package com.guestbook.repository;

import com.guestbook.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEntryIdOrderByCreatedAtAsc(Long entryId);
    long countByEntryId(Long entryId);
    void deleteByEntryId(Long entryId);
}
