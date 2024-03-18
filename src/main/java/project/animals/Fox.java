package project.animals;
import project.Animal;
import project.Organism;
import project.World;
import project.Point;
import java.util.Random;
import java.awt.*;

public class Fox extends Animal {
    private static final int FOX_MOVE_RANGE = 1;
    private static final int FOX_MOVE_CHANCE = 1;
    private static final int FOX_STRENGTH = 3;
    private static final int FOX_INITIATIVE = 7;

    @Override
    public String OrganismTypeToString() {
        return "FOX";
    }

    public Fox(World world, Point position, int birthRound) {
        super(OrganismType.FOX, world, position, birthRound, FOX_STRENGTH, FOX_INITIATIVE);
        this.setMoveRange(FOX_MOVE_RANGE);
        this.setMoveChance(FOX_MOVE_CHANCE);
        setOrganismColor(new Color(255, 125, 0));
    }

    @Override
    public Point DrawRandomField(Point position) {
        UnlockAllDirections();
        int positionX = position.getX();
        int positionY = position.getY();
        int sizeX = getWorld().getSizeX();
        int sizeY = getWorld().getSizeY();
        int possibleDirections = 0;
        Organism tmpOrganism;

        if (positionX == 0) LockDirection(Direction.LEFT);
        else {
            tmpOrganism = getWorld().getBoard()[positionY][positionX - 1];
            if (tmpOrganism != null && tmpOrganism.getStrength() > this.getStrength()) {
                LockDirection(Direction.LEFT);
            } else possibleDirections++;
        }

        if (positionX == sizeX - 1) LockDirection(Direction.RIGHT);
        else {
            tmpOrganism = getWorld().getBoard()[positionY][positionX + 1];
            if (tmpOrganism != null && tmpOrganism.getStrength() > this.getStrength()) {
                LockDirection(Direction.RIGHT);
            } else possibleDirections++;
        }

        if (positionY == 0) LockDirection(Direction.UP);
        else {
            tmpOrganism = getWorld().getBoard()[positionY - 1][positionX];
            if (tmpOrganism != null && tmpOrganism.getStrength() > this.getStrength()) {
                LockDirection(Direction.UP);
            } else possibleDirections++;
        }

        if (positionY == sizeY - 1) LockDirection(Direction.DOWN);
        else {
            tmpOrganism = getWorld().getBoard()[positionY + 1][positionX];
            if (tmpOrganism != null && tmpOrganism.getStrength() > this.getStrength()) {
                LockDirection(Direction.DOWN);
            } else possibleDirections++;
        }

        if (possibleDirections == 0) {
            return new Point(positionX, positionY);
        }
        while (true) {
            Random rnd = new Random();
            int upperBound = 100;
            int number = rnd.nextInt(upperBound);
            //LEFT
            if (number < 25 && !IfDirectionLocked(Direction.LEFT))
                return new Point(positionX - 1, positionY);
            //RIGHT
            else if (number >= 25 && number < 50 && !IfDirectionLocked(Direction.RIGHT))
                return new Point(positionX + 1, positionY);
            //UP
            else if (number >= 50 && number < 75 && !IfDirectionLocked(Direction.UP))
                return new Point(positionX, positionY - 1);
            //DOWN
            else if (number >= 75 && !IfDirectionLocked(Direction.DOWN))
                return new Point(positionX, positionY + 1);
        }
    }
}
