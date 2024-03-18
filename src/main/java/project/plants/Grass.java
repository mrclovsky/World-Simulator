package project.plants;
import project.Plant;
import project.World;
import project.Point;
import java.awt.*;

public class Grass extends Plant {
    private static final int GRASS_STRENGTH = 0;
    private static final int GRASS_INITIATIVE = 0;

    @Override
    public String OrganismTypeToString() {
        return "GRASS";
    }

    public Grass(World world, Point position, int birthRound) {
        super(OrganismType.GRASS, world, position, birthRound, GRASS_STRENGTH, GRASS_INITIATIVE);
        setOrganismColor(Color.GREEN);
    }

}
