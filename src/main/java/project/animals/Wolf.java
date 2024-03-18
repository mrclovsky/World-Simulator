package project.animals;
import project.Animal;
import project.World;
import project.Point;
import java.awt.*;

public class Wolf extends Animal {
    private static final int WOLF_MOVE_RANGE = 1;
    private static final int WOLF_MOVE_CHANCE = 1;
    private static final int WOLF_STRENGTH = 9;
    private static final int WOLF_INITIATIVE = 5;

    @Override
    public String OrganismTypeToString() {
        return "WOLF";
    }

    public Wolf(World world, Point position, int birthRound) {
        super(OrganismType.WOLF, world, position, birthRound, WOLF_STRENGTH, WOLF_INITIATIVE);
        this.setMoveRange(WOLF_MOVE_RANGE);
        this.setMoveChance(WOLF_MOVE_CHANCE);
        setOrganismColor(Color.LIGHT_GRAY);
    }

}
