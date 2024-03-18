package project;
import java.awt.*;
import java.util.Random;

public abstract class Organism {
    public enum OrganismType {
        HUMAN,
        WOLF,
        SHEEP,
        FOX,
        TURTLE,
        ANTELOPE,
        CYBERSHEEP,
        GRASS,
        DANDELION,
        GUARANA,
        NIGHTSHADE,
        SOSNOWSKYHOGWEED;
    }

    public enum Direction {
        LEFT(0),
        RIGHT(1),
        UP(2),
        DOWN(3),
        NODIRECTION(4);

        private final int value;

        private Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private int strength;
    private int initiative;
    private int birthRound;
    private Color organismColor;
    private boolean ifDead;
    private boolean[] direction;
    private boolean ifBreeded;
    private World world;
    private Point position;
    private OrganismType organismType;
    private double breedChance;
    private static final int DISTINCT_SPECIES_NUMBER = 12;

    public abstract String OrganismTypeToString();

    public abstract void Action();

    public abstract void Colision(Organism other);

    public abstract boolean IfAnimal();

    public Organism(OrganismType organismType, World world, Point position, int birthRound, int strength, int initiative) {
        this.organismType = organismType;
        this.world = world;
        this.position = position;
        this.birthRound = birthRound;
        this.strength = strength;
        this.initiative = initiative;
        ifDead = false;
        direction = new boolean[]{true, true, true, true};
    }

    public boolean SpecialAction(Organism attacker, Organism victim) {
        return false;
    }

    public String OrganismToString() {
        return (OrganismTypeToString() + " [" + position.getX() + "," + position.getY() + "][STR" + strength + "]");
    }

    public void MakeMove(Point futurePosition) {
        int x = futurePosition.getX();
        int y = futurePosition.getY();
        world.getBoard()[position.getY()][position.getX()] = null;
        world.getBoard()[y][x] = this;
        position.setX(x);
        position.setY(y);
    }

    static OrganismType DrawOrganismType() {
        Random rnd = new Random();
        int number = rnd.nextInt(DISTINCT_SPECIES_NUMBER - 1);
        if (number == 0) return OrganismType.ANTELOPE;
        if (number == 1) return OrganismType.SOSNOWSKYHOGWEED;
        if (number == 2) return OrganismType.GUARANA;
        if (number == 3) return OrganismType.FOX;
        if (number == 4) return OrganismType.DANDELION;
        if (number == 5) return OrganismType.SHEEP;
        if (number == 6) return OrganismType.GRASS;
        if (number == 7) return OrganismType.NIGHTSHADE;
        if (number == 8) return OrganismType.WOLF;
        if (number == 9) return OrganismType.CYBERSHEEP;
        else return OrganismType.TURTLE;
    }

    public Point DrawRandomField(Point position) {
        UnlockAllDirections();
        int positionX = position.getX();
        int positionY = position.getY();
        int sizeX = world.getSizeX();
        int sizeY = world.getSizeY();
        int possibleDirections = 0;

        if (positionX == 0) LockDirection(Direction.LEFT);
        else possibleDirections++;
        if (positionX == sizeX - 1) LockDirection(Direction.RIGHT);
        else possibleDirections++;
        if (positionY == 0) LockDirection(Direction.UP);
        else possibleDirections++;
        if (positionY == sizeY - 1) LockDirection(Direction.DOWN);
        else possibleDirections++;

        if (possibleDirections == 0) return position;
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

    public Point DrawUnoccupiedField(Point position) {
        UnlockAllDirections();
        int positionX = position.getX();
        int positionY = position.getY();
        int sizeX = world.getSizeX();
        int sizeY = world.getSizeY();
        int possibleDirections = 0;

        if (positionX == 0) LockDirection(Direction.LEFT);
        else {
            if (world.IfFieldOccupied(new Point(positionX - 1, positionY)) == false) possibleDirections++;
            else LockDirection(Direction.LEFT);
        }

        if (positionX == sizeX - 1) LockDirection(Direction.RIGHT);
        else {
            if (world.IfFieldOccupied(new Point(positionX + 1, positionY)) == false) possibleDirections++;
            else LockDirection(Direction.RIGHT);
        }

        if (positionY == 0) LockDirection(Direction.UP);
        else {
            if (world.IfFieldOccupied(new Point(positionX, positionY - 1)) == false) possibleDirections++;
            else LockDirection(Direction.UP);
        }

        if (positionY == sizeY - 1) LockDirection(Direction.DOWN);
        else {
            if (world.IfFieldOccupied(new Point(positionX, positionY + 1)) == false) possibleDirections++;
            else LockDirection(Direction.DOWN);
        }

        if (possibleDirections == 0) return new Point(positionX, positionY);
        while (true) {
            Random rnd = new Random();
            int upperBound = 100;
            int number = rnd.nextInt(upperBound);
            //LEFT
            if (number < 25 && !IfDirectionLocked(Direction.LEFT))
                return new Point(positionX - 1, positionY);
            //LEFT
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

    protected void LockDirection(Direction direction) {
        this.direction[direction.getValue()] = false;
    }

    protected void UnlockDirection(Direction direction) {
        this.direction[direction.getValue()] = true;
    }

    protected void UnlockAllDirections() {
        UnlockDirection(Direction.LEFT);
        UnlockDirection(Direction.RIGHT);
        UnlockDirection(Direction.UP);
        UnlockDirection(Direction.DOWN);
    }

    protected boolean IfDirectionLocked(Direction direction) {
        return !(this.direction[direction.getValue()]);
    }

    public int getStrength() {
        return strength;
    }

    public int getInitiative() {
        return initiative;
    }

    public int getBirthRound() {
        return birthRound;
    }

    public boolean getIfDead() {
        return ifDead;
    }

    public boolean getIfBreeded() {
        return ifBreeded;
    }

    public World getWorld() {
        return world;
    }

    public Point getPosition() {
        return position;
    }

    public OrganismType getOrganismType() {
        return organismType;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public void setBirthRound(int birthRound) {
        this.birthRound = birthRound;
    }

    public void setIfDead(boolean ifDead) {
        this.ifDead = ifDead;
    }

    public void setIfBreeded(boolean ifBreeded) {
        this.ifBreeded = ifBreeded;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setOrganismType(OrganismType organismType) {
        this.organismType = organismType;
    }

    public Color getOrganismColor() {
        return organismColor;
    }

    public void setOrganismColor(Color organismColor) {
        this.organismColor = organismColor;
    }

    public double getBreedChance() {
        return breedChance;
    }

    public void setBreedChance(double breedChance) {
        this.breedChance = breedChance;
    }
}
