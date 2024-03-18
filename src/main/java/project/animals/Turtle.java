package project.animals;
import project.Animal;
import project.Organism;
import project.Messenger;
import project.World;
import project.Point;
import java.awt.*;

public class Turtle extends Animal {
    private static final int TURTLE_MOVE_RANGE = 1;
    private static final double TURTLE_MOVE_CHANCE = 0.25;
    private static final int TURTLE_STRENGTH = 2;
    private static final int TURTLE_INITIATIVE = 1;

    @Override
    public String OrganismTypeToString() {
        return "TURTLE";
    }

    public Turtle(World world, Point position, int birthRound) {
        super(OrganismType.TURTLE, world, position, birthRound, TURTLE_STRENGTH, TURTLE_INITIATIVE);
        this.setMoveRange(TURTLE_MOVE_RANGE);
        this.setMoveChance(TURTLE_MOVE_CHANCE);
        setOrganismColor(new Color(0, 100, 0));
    }

    @Override
    public boolean SpecialAction(Organism attacker, Organism victim) {
        if (this == victim) {
            if (attacker.getStrength() < 5 && attacker.IfAnimal()) {
                Messenger.AddMessage(OrganismToString() + " repels " + attacker.OrganismToString());
                return true;
            } else return false;
        } else {
            if (attacker.getStrength() >= victim.getStrength()) return false;
            else {
                if (victim.getStrength() < 5 && victim.IfAnimal()) {
                    Messenger.AddMessage(OrganismToString() + " repels " + victim.OrganismToString());
                    return true;
                } else return false;
            }
        }
    }
}