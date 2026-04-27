package com.guestbook.model;

public class GuestBookStats {
    private long totalEntries;
    private long todayEntries;
    private long totalLikes;

    public GuestBookStats(long totalEntries, long todayEntries, long totalLikes) {
        this.totalEntries = totalEntries;
        this.todayEntries = todayEntries;
        this.totalLikes = totalLikes;
    }

    public long getTotalEntries() { return totalEntries; }
    public long getTodayEntries() { return todayEntries; }
    public long getTotalLikes() { return totalLikes; }
}
