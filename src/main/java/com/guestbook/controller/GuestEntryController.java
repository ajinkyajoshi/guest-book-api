package com.guestbook.controller;

import com.guestbook.model.GuestBookStats;
import com.guestbook.model.GuestEntry;
import com.guestbook.service.GuestEntryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entries")
@CrossOrigin(origins = "*")
public class GuestEntryController {

    private final GuestEntryService service;

    public GuestEntryController(GuestEntryService service) {
        this.service = service;
    }

    @GetMapping
    public Page<GuestEntry> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        if (search != null && !search.isBlank()) {
            return service.searchEntries(search, page, size);
        }
        return service.getAllEntries(page, size);
    }

    @GetMapping("/stats")
    public GuestBookStats getStats() {
        return service.getStats();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GuestEntry create(@Valid @RequestBody GuestEntry entry) {
        return service.createEntry(entry);
    }

    @PutMapping("/{id}")
    public GuestEntry update(@PathVariable Long id, @Valid @RequestBody GuestEntry entry) {
        return service.updateEntry(id, entry);
    }

    @PatchMapping("/{id}/like")
    public GuestEntry like(@PathVariable Long id) {
        return service.toggleLike(id);
    }

    @PatchMapping("/{id}/pin")
    public GuestEntry pin(@PathVariable Long id) {
        return service.togglePin(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteEntry(id);
    }
}
