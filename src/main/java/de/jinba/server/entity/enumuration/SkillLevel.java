package de.jinba.server.entity.enumuration;

public enum SkillLevel {
    BEGINNER("Beginner", "0 - 1 years"),
    INTERMEDIATE("Intermediate", "1 - 3 years"),
    ADVANCED ("Advanced", " 3 - 5 years"),
    SPECIALIST ("Specialist", " 5+ years");
    private final String skillLevel;
    private final String years;
    SkillLevel(String skillLevel, String years) {
        this.skillLevel = skillLevel;
        this.years = years;
    }

    public String skillLevel() {
        return skillLevel;
    }
    public String years(){return years;}
}
