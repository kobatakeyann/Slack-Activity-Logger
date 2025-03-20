package com.suu.hppa.slack_activity_logger.model;

public class User {
    private final String ID;
    private final String NAME;
    private boolean isPresent = false;

    public User(String id, String name) {
        this.ID = id;
        this.NAME = name;
    }

    public String getId() {
        return this.ID;
    }

    public String getName() {
        return this.NAME;
    }

    public boolean isPresent() {
        return this.isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    @Override
    public String toString() {
        return "id: %s, name: %-10s, present: %b".formatted(this.ID, this.NAME, this.isPresent);
    }
}
