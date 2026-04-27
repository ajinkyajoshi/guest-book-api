package com.guestbook.service;

import com.guestbook.model.GuestBookStats;
import com.guestbook.model.GuestEntry;
import com.guestbook.repository.GuestEntryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

@Service
public class GuestEntryService {

    private final GuestEntryRepository repository;

    public GuestEntryService(GuestEntryRepository repository) {
        this.repository = repository;
    }

    public Page<GuestEntry> getAllEntries(int page, int size) {
        return repository.findAllByOrderByPinnedDescCreatedAtDesc(PageRequest.of(page, size));
    }

    public Page<GuestEntry> searchEntries(String query, int page, int size) {
        return repository.search(query, PageRequest.of(page, size));
    }

    public GuestEntry createEntry(GuestEntry entry) {
        return repository.save(entry);
    }

    public GuestEntry updateEntry(Long id, GuestEntry updated) {
        GuestEntry entry = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found"));
        entry.setName(updated.getName());
        entry.setEmail(updated.getEmail());
        entry.setMessage(updated.getMessage());
        entry.setMood(updated.getMood());
        return repository.save(entry);
    }

    public GuestEntry toggleLike(Long id) {
        GuestEntry entry = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found"));
        entry.setLikes(entry.getLikes() + 1);
        return repository.save(entry);
    }

    public GuestEntry togglePin(Long id) {
        GuestEntry entry = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found"));
        entry.setPinned(!entry.isPinned());
        return repository.save(entry);
    }

    public void deleteEntry(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Entry not found");
        }
        repository.deleteById(id);
    }

    public GuestBookStats getStats() {
        long total = repository.count();
        long today = repository.countByCreatedAtAfter(LocalDate.now().atStartOfDay());
        long likes = repository.sumAllLikes();
        return new GuestBookStats(total, today, likes);
    }
}
