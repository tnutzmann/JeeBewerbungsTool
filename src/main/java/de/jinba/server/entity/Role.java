package de.jinba.server.entity;

public enum Role {
    DEFAULT_USER("DEFAULT_USER"), ADMIN("ADMIN"), COMPANY_USER("COMPANY_USER");
    private final String role;

    Role(String role) {
        this.role = role;
    }
    public String role() {
        return role;
    }
}
