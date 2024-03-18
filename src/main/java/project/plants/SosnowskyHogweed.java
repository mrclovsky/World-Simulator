package project.plants;
import project.Organism;
import project.Plant;
import project.Messenger;
import project.World;
import project.Point;
import java.awt.*;
import java.util.Random;

public class SosnowskyHogweed extends Plant {
    private static final int SOSNOWSKYHOGWEED_STRENGTH = 10;
    private static final int SOSNOWSKYHOGWEED_INITIATIVE = 0;

    @Override
    public String OrganismTypeToString() {
        return "SOSNOWSKY HOGWEED";
    }

    public SosnowskyHogweed(World world, Point position, int birthRound) {

        super(OrganismType.SOSNOWSKYHOGWEED, world, position, birthRound, SOSNOWSKYHOGWEED_STRENGTH, SOSNOWSKYHOGWEED_INITIATIVE);
        setOrganismColor(Color.CYAN);
        setBreedChance(0.05);
    }

    @Override
    public void Action() {
        int positionX = getPosition().getX();
        int positionY = getPosition().getY();
        DrawRandomField(getPosition());
        for (int i = 0; i < 4; i++) {
            Organism tmpOrganism = null;
            if (i == 0 && !IfDirectionLocked(Direction.DOWN))
                tmpOrganism = getWorld().GetFieldOccupant(new Point(positionX, positionY + 1));
            else if (i == 1 && !IfDirectionLocked(Direction.UP))
                tmpOrganism = getWorld().GetFieldOccupant(new Point(positionX, positionY - 1));
            else if (i == 2 && !IfDirectionLocked(Direction.LEFT))
                tmpOrganism = getWorld().GetFieldOccupant(new Point(positionX - 1, positionY));
            else if (i == 3 && !IfDirectionLocked(Direction.RIGHT))
                tmpOrganism = getWorld().GetFieldOccupant(new Point(positionX + 1, positionY));

            if (tmpOrganism != null && tmpOrganism.IfAnimal() && tmpOrganism.getOrganismType() != OrganismType.CYBERSHEEP) {
                getWorld().DeleteOrganism(tmpOrganism);
                Messenger.AddMessage(OrganismToString() + " killed " + tmpOrganism.OrganismToString());
            }
        }
        Random rnd = new Random();
        int number = rnd.nextInt(100);
        if (number < getBreedChance() * 100) Spreading();
    }

    @Override
    public boolean SpecialAction(Organism attacker, Organism victim) {
        if (attacker.getStrength() >= 10) {
            getWorld().DeleteOrganism(this);
            Messenger.AddMessage(attacker.OrganismToString() + " ate " + this.OrganismToString());
            attacker.MakeMove(victim.getPosition());
        }
        if ((attacker.IfAnimal() && attacker.getOrganismType() != OrganismType.CYBERSHEEP) || attacker.getStrength() < 10) {
            getWorld().DeleteOrganism(attacker);
            Messenger.AddMessage(this.OrganismToString() + " killed " + attacker.OrganismToString());
        }
        return true;
    }
}
