package project.animals;
import project.Animal;
import project.World;
import project.Point;
import java.awt.*;

public class Sheep extends Animal {
    private static final int SHEEP_MOVE_RANGE = 1;
    private static final int SHEEP_MOVE_CHANCE = 1;
    private static final int SHEEP_STRENGTH = 4;
    private static final int SHEEP_INITIATIVE = 4;

    @Override
    public String OrganismTypeToString() {
        return "SHEEP";
    }

    public Sheep(World world, Point position, int birthRound) {
        super(OrganismType.SHEEP, world, position, birthRound, SHEEP_STRENGTH, SHEEP_INITIATIVE);
        this.setMoveRange(SHEEP_MOVE_RANGE);
        this.setMoveChance(SHEEP_MOVE_CHANCE);
        setOrganismColor(Color.PINK);
    }

}

