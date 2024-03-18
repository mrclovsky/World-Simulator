package project;
import java.util.Random;

public abstract class Plant extends Organism {

    protected Plant(OrganismType organismType, World world, Point position, int birthRound, int strength, int initiative) {
        super(organismType, world, position, birthRound, strength, initiative);
        setBreedChance(0.3);
    }

    @Override
    public void Action() {
        Random rnd = new Random();
        int upperBound = 100;
        int number = rnd.nextInt(upperBound);
        if (number < getBreedChance() * 100) Spreading();
    }

    @Override
    public boolean IfAnimal() {
        return false;
    }

    protected void Spreading() {
        Point tmpPoint = this.DrawUnoccupiedField(getPosition());
        if (tmpPoint.equals(getPosition())) return;
        else {
            Organism tmpOrganism = OrganismFactory.CreateOrganism(getOrganismType(), this.getWorld(), tmpPoint);
            Messenger.AddMessage("A new plant has grown - " + tmpOrganism.OrganismToString());
            getWorld().AddOrganism(tmpOrganism);
        }
    }

    @Override
    public void Colision(Organism other) {}
}
