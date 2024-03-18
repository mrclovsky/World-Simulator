package project.plants;
import project.Plant;
import project.World;
import project.Point;
import java.util.Random;
import java.awt.*;

public class Dandelion extends Plant {
    private static final int DANDELION_STRENGTH = 0;
    private static final int DANDELION_INITIATIVE = 0;
    private static final int NUMBER_OF_TRIES = 3;

    @Override
    public String OrganismTypeToString() {
        return "DANDELION";
    }

    public Dandelion(World world, Point position, int birthRound) {
        super(OrganismType.DANDELION, world, position, birthRound, DANDELION_STRENGTH, DANDELION_INITIATIVE);
        setOrganismColor(Color.YELLOW);
    }

    @Override
    public void Action() {
        Random rnd = new Random();
        for (int i = 0; i < NUMBER_OF_TRIES; i++) {
            int number = rnd.nextInt(100);
            if (number < getBreedChance()) Spreading();
        }
    }

}

