package project.plants;
import project.Plant;
import project.Organism;
import project.Messenger;
import project.World;
import project.Point;
import java.util.Random;
import java.awt.*;

public class Nightshade extends Plant {
    private static final int NIGHTSHADE_STRENGTH = 99;
    private static final int NIGHTSHADE_INITIATIVE = 0;

    @Override
    public String OrganismTypeToString() {
        return "NIGHTSHADE";
    }

    public Nightshade(World world, Point position, int birthRound) {
        super(OrganismType.NIGHTSHADE, world, position, birthRound, NIGHTSHADE_STRENGTH, NIGHTSHADE_INITIATIVE);
        setOrganismColor(new Color(20, 20, 80));
        setBreedChance(0.05);
    }


    @Override
    public void Action() {
        Random rnd = new Random();
        int upperBound = 100;
        int number = rnd.nextInt(upperBound);
        if (number < getBreedChance() * 100) Spreading();
    }

    @Override
    public boolean SpecialAction(Organism attacker, Organism victim) {
        Messenger.AddMessage(attacker.OrganismToString() + " ate " + this.OrganismToString());
        if (attacker.getStrength() >= 99) {
            getWorld().DeleteOrganism(this);
            Messenger.AddMessage(attacker.OrganismToString() + " destroyed " + this.OrganismToString());
        }
        if (attacker.IfAnimal()) {
            getWorld().DeleteOrganism(attacker);
            Messenger.AddMessage(this.OrganismToString() + " killed " + attacker.OrganismToString());
        }
        return true;
    }
}

