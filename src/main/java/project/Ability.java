package project;

public class Ability {
    private final int ABILITY_DURATION = 5;
    private final int ABILITY_COOLDOWN = 10;
    private boolean ifCanActivate;
    private boolean ifActive;
    private int duration;
    private int cooldown;

    public Ability() {
        cooldown = 0;
        duration = 0;
        ifActive = false;
        ifCanActivate = true;
    }

    public void CheckConditions() {
        if (cooldown > 0) cooldown--;
        if (duration > 0) duration--;
        if (duration == 0) Deactivate();
        if (cooldown == 0) ifCanActivate = true;
    }

    public void Activate() {
        if (cooldown == 0) {
            ifActive = true;
            ifCanActivate = false;
            cooldown = ABILITY_COOLDOWN;
            duration = ABILITY_DURATION;
        }
    }

    public void Deactivate() {
        ifActive = false;
    }

    public boolean getIfCanActivate() {
        return ifCanActivate;
    }

    public void setIfCanActivate(boolean ifCanActivate) {
        this.ifCanActivate = ifCanActivate;
    }

    public boolean getIfActive() {
        return ifActive;
    }

    public void setIfActive(boolean ifActive) {
        this.ifActive = ifActive;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }
}
