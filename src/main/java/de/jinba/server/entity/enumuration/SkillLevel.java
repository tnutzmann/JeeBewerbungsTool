package de.jinba.server.entity.enumuration;

public enum SkillLevel {
    BEGINNER("Beginner: 0 - 1 year"),
    INTERMEDIATE("Intermediate: 1 - 3 years"),
    ADVANCED ("Advanced: 3 - 5 years"),
    SPECIALIST ("Specialist: 5+ years");
    private final String skillLevel;
    SkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public String skillLevel() {
        return skillLevel;
    }
}
