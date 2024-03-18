package project.animals;
import project.Organism;
import project.Point;
import project.World;
import project.plants.SosnowskyHogweed;
import java.awt.*;

public class CyberSheep extends Sheep {
    private static final int CYBERSHEEP_MOVE_RANGE = 1;
    private static final int CYBERSHEEP_MOVE_CHANCE = 1;
    private static final int CYBERSHEEP_STRENGTH = 11;
    private static final int CYBERSHEEP_INITIATIVE = 4;

    @Override
    public String OrganismTypeToString() {
        return "CYBER SHEEP";
    }

    public CyberSheep(World world, Point position, int birthRound) {
        super(world, position, birthRound);
        setOrganismType(OrganismType.CYBERSHEEP);
        setStrength(CYBERSHEEP_STRENGTH);
        setInitiative(CYBERSHEEP_INITIATIVE);
        setBreedChance(0.1);

        this.setMoveRange(CYBERSHEEP_MOVE_RANGE);
        this.setMoveChance(CYBERSHEEP_MOVE_CHANCE);
        setOrganismColor(Color.BLACK);
    }

    @Override
    public Point DrawRandomField(Point position) {
        if (getWorld().ifHogweedExists()) {

            Point target = FindHogweed().getPosition();
            int dx = Math.abs(position.getX() - target.getX());
            int dy = Math.abs(position.getY() - target.getY());
            if (dx >= dy) {
                if (position.getX() > target.getX()) {
                    return new Point(position.getX() - 1, position.getY());
                } else {
                    return new Point(position.getX() + 1, position.getY());
                }
            } else {
                if (position.getY() > target.getY()) {
                    return new Point(position.getX(), position.getY() - 1);
                } else {
                    return new Point(position.getX(), position.getY() + 1);
                }
            }
        } else return super.DrawRandomField(position);
    }

    private SosnowskyHogweed FindHogweed() {
        SosnowskyHogweed tmpHogweed = null;
        int shortestDistance = getWorld().getSizeX() + getWorld().getSizeY() + 1;
        for (int i = 0; i < getWorld().getSizeY(); i++) {
            for (int j = 0; j < getWorld().getSizeX(); j++) {
                Organism tmpOrganism = getWorld().getBoard()[i][j];
                if (tmpOrganism != null &&
                        tmpOrganism.getOrganismType() == OrganismType.SOSNOWSKYHOGWEED) {
                    int distance = CalculateDistance(tmpOrganism.getPosition());
                    if (shortestDistance > distance) {
                        shortestDistance = distance;
                        tmpHogweed = (SosnowskyHogweed)tmpOrganism;
                    }
                }
            }
        }
        return tmpHogweed;
    }

    private int CalculateDistance(Point position) {
        int dx = Math.abs(getPosition().getX() - position.getX());
        int dy = Math.abs(getPosition().getY() - position.getY());
        return dx + dy;
    }

}
