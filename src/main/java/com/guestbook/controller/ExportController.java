package com.guestbook.controller;

import com.guestbook.model.GuestEntry;
import com.guestbook.repository.GuestEntryRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "*")
public class ExportController {

    private final GuestEntryRepository repository;

    public ExportController(GuestEntryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=guestbook_entries.csv");

        List<GuestEntry> entries = repository.findAll();
        PrintWriter writer = response.getWriter();
        writer.println("ID,Name,Email,Message,Mood,Likes,Pinned,Created At");
        for (GuestEntry e : entries) {
            writer.printf("%d,\"%s\",\"%s\",\"%s\",%s,%d,%b,%s%n",
                    e.getId(),
                    escapeCsv(e.getName()),
                    escapeCsv(e.getEmail() != null ? e.getEmail() : ""),
                    escapeCsv(e.getMessage()),
                    e.getMood(),
                    e.getLikes(),
                    e.isPinned(),
                    e.getCreatedAt());
        }
        writer.flush();
    }

    private String escapeCsv(String value) {
        return value.replace("\"", "\"\"");
    }
}
