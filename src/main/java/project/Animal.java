package project;
import java.util.Random;

public abstract class Animal extends Organism {
    private int moveRange;
    private double moveChance;

    public Animal(OrganismType organismType, World world, Point position, int birthRound, int strength, int initiative) {
        super(organismType, world, position, birthRound, strength, initiative);
        setIfBreeded(false);
        setBreedChance(0.5);
    }

    @Override
    public void Action() {
        for (int i = 0; i < moveRange; i++) {
            Point futurePosition = PlanMove();

            if (getWorld().IfFieldOccupied(futurePosition) && getWorld().GetFieldOccupant(futurePosition) != this) {
                Colision(getWorld().GetFieldOccupant(futurePosition));
                break;
            } else if (getWorld().GetFieldOccupant(futurePosition) != this) {
                MakeMove(futurePosition);
            }
        }
    }

    @Override
    public void Colision(Organism other) {
        if (getOrganismType() == other.getOrganismType()) {
            Random rnd = new Random();
            int number = rnd.nextInt(100);
            if (number < getBreedChance() * 100) Breeding(other);
        } else {
            if (other.SpecialAction(this, other)) return;
            if (SpecialAction(this, other)) return;

            if (getStrength() >= other.getStrength()) {
                getWorld().DeleteOrganism(other);
                MakeMove(other.getPosition());
                Messenger.AddMessage(OrganismToString() + " killed " + other.OrganismToString());
            } else {
                getWorld().DeleteOrganism(this);
                Messenger.AddMessage(other.OrganismToString() + " killed " + OrganismToString());
            }
        }
    }

    @Override
    public boolean IfAnimal() {
        return true;
    }

    protected Point PlanMove() {
        Random rnd = new Random();
        int upperBound = 100;
        int number = rnd.nextInt(upperBound);
        if (number >= (int) (moveChance * 100)) return getPosition();
        else return DrawRandomField(getPosition());
    }

    private void Breeding(Organism other) {
        if (this.getIfBreeded() || other.getIfBreeded()) return;
        Point tmpPoint1 = this.DrawUnoccupiedField(getPosition());

        if (tmpPoint1.equals(getPosition())) {
            Point tmpPoint2 = other.DrawUnoccupiedField(other.getPosition());
            if (tmpPoint2.equals(other.getPosition())) return;
            else {
                Organism tmpOrganism = OrganismFactory.CreateOrganism(getOrganismType(), this.getWorld(), tmpPoint2);
                Messenger.AddMessage(tmpOrganism.OrganismToString() + " has been born");
                getWorld().AddOrganism(tmpOrganism);
                setIfBreeded(true);
                other.setIfBreeded(true);
            }
        } else {
            Organism tmpOrganism = OrganismFactory.CreateOrganism(getOrganismType(), this.getWorld(), tmpPoint1);
            Messenger.AddMessage(tmpOrganism.OrganismToString() + " has been born");
            getWorld().AddOrganism(tmpOrganism);
            setIfBreeded(true);
            other.setIfBreeded(true);
        }
    }

    public int getMoveRange() {
        return moveRange;
    }

    public void setMoveRange(int moveRange) {
        this.moveRange = moveRange;
    }

    public double getMoveChance() {
        return moveChance;
    }

    public void setMoveChance(double moveChance) {
        this.moveChance = moveChance;
    }
}
