package com.guestbook.controller;

import com.guestbook.model.Comment;
import com.guestbook.repository.CommentRepository;
import com.guestbook.repository.GuestEntryRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/entries/{entryId}/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentRepository commentRepo;
    private final GuestEntryRepository entryRepo;

    public CommentController(CommentRepository commentRepo, GuestEntryRepository entryRepo) {
        this.commentRepo = commentRepo;
        this.entryRepo = entryRepo;
    }

    @GetMapping
    public List<Comment> getComments(@PathVariable Long entryId) {
        return commentRepo.findByEntryIdOrderByCreatedAtAsc(entryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@PathVariable Long entryId, @Valid @RequestBody Comment comment) {
        if (!entryRepo.existsById(entryId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found");
        }
        comment.setEntryId(entryId);
        return commentRepo.save(comment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long entryId, @PathVariable Long commentId) {
        commentRepo.deleteById(commentId);
    }
}
