package com.stemroom.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** A music project. The owner and any added musicians can open it. */
public class Project {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("d MMM yyyy");

    private final int id;
    private final String title;
    private final String description;
    private final int ownerId;
    private final String ownerName;
    private final LocalDate deadline;
    private final ProjectStatus status;

    public Project(int id, String title, String description, int ownerId,
                   String ownerName, LocalDate deadline, ProjectStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.deadline = deadline;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    /** The deadline as a person would read it, such as "20 Nov 2026". */
    public String getDeadlineText() {
        return deadline == null ? "none set" : deadline.format(DATE_FORMAT);
    }
}
