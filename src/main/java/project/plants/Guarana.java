package project.plants;
import project.Plant;
import project.Organism;
import project.Messenger;
import project.World;
import project.Point;
import java.awt.*;

public class Guarana extends Plant {

    private static final int GUARANA_STRENGTH = 0;
    private static final int GUARANA_INITIATIVE = 0;
    private static final int STRENGTH_INCREASE = 3;

    @Override
    public String OrganismTypeToString() {
        return "GUARANA";
    }

    public Guarana(World world, Point position, int birthRound) {
        super(OrganismType.GUARANA, world, position, birthRound, GUARANA_STRENGTH, GUARANA_INITIATIVE);
        setOrganismColor(Color.RED);
    }

    @Override
    public boolean SpecialAction(Organism attacker, Organism victim) {
        Point tmpPosition = this.getPosition();
        getWorld().DeleteOrganism(this);
        attacker.MakeMove(tmpPosition);
        Messenger.AddMessage(attacker.OrganismToString() + " ate " + this.OrganismToString() + " and increased it's strength by " + Integer.toString(STRENGTH_INCREASE));
        attacker.setStrength(attacker.getStrength() + STRENGTH_INCREASE);
        return true;
    }
}
