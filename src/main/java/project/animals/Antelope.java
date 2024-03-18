package project.animals;
import project.Animal;
import project.Organism;
import project.Messenger;
import project.World;
import project.Point;
import java.util.Random;
import java.awt.*;

public class Antelope extends Animal {
    private static final int ANTELOPE_MOVE_RANGE = 2;
    private static final int ANTELOPE_MOVE_CHANCE = 1;
    private static final int ANTELOPE_STRENGTH = 4;
    private static final int ANTELOPE_INITIATIVE = 4;

    @Override
    public String OrganismTypeToString() {
        return "ANTELOPE";
    }

    public Antelope(World world, Point position, int birthRound) {
        super(OrganismType.ANTELOPE, world, position, birthRound, ANTELOPE_STRENGTH, ANTELOPE_INITIATIVE);
        this.setMoveRange(ANTELOPE_MOVE_RANGE);
        this.setMoveChance(ANTELOPE_MOVE_CHANCE);
        setOrganismColor(new Color(150, 70, 0));
    }

    @Override
    public boolean SpecialAction(Organism attacker, Organism victim) {
        Random rnd = new Random();
        int number = rnd.nextInt(100);
        if (number < 50) {
            if (this == attacker) {
                Messenger.AddMessage(OrganismToString() + " flees from " + victim.OrganismToString());
                Point tmpPosition = DrawUnoccupiedField(victim.getPosition());
                if (!tmpPosition.equals(victim.getPosition()))
                    MakeMove(tmpPosition);
            } else if (this == victim) {
                Messenger.AddMessage(OrganismToString() + " flees from " + attacker.OrganismToString());
                Point tmpPosition = this.getPosition();
                MakeMove(DrawUnoccupiedField(this.getPosition()));
                if (getPosition().equals(tmpPosition)) {
                    getWorld().DeleteOrganism(this);
                    Messenger.AddMessage(attacker.OrganismToString() + " killed " + OrganismToString());
                }
                attacker.MakeMove(tmpPosition);
            }
            return true;
        } else return false;
    }
}
