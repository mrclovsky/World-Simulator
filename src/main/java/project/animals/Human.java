package project.animals;
import project.Animal;
import project.Organism;
import project.Messenger;
import project.World;
import project.Point;
import project.Ability;
import java.awt.*;

public class Human extends Animal {
    private static final int HUMAN_MOVE_RANGE = 1;
    private static final int HUMAN_MOVE_CHANCE = 1;
    private static final int HUMAN_STRENGTH = 5;
    private static final int HUMAN_INITIATIVE = 4;
    private Direction movementDirection;
    private Ability ability;

    @Override
    public String OrganismTypeToString() {
        return "HUMAN";
    }
    public Human(World world, Point position, int birthRound) {
        super(OrganismType.HUMAN, world, position, birthRound, HUMAN_STRENGTH, HUMAN_INITIATIVE);
        this.setMoveRange(HUMAN_MOVE_RANGE);
        this.setMoveChance(HUMAN_MOVE_CHANCE);
        movementDirection = Direction.NODIRECTION;
        setOrganismColor(Color.BLUE);
        ability = new Ability();
    }

    private void Scorch() {
        DrawRandomField(getPosition());
        int x = getPosition().getX();
        int y = getPosition().getY();
        Organism tmpOrganism = null;
        for (int i = 0; i < 4; i++) {
            if (i == 0 && !IfDirectionLocked(Direction.DOWN))
                tmpOrganism = getWorld().GetFieldOccupant(new Point(x, y + 1));
            else if (i == 1 && !IfDirectionLocked(Direction.UP))
                tmpOrganism = getWorld().GetFieldOccupant(new Point(x, y - 1));
            else if (i == 2 && !IfDirectionLocked(Direction.LEFT))
                tmpOrganism = getWorld().GetFieldOccupant(new Point(x - 1, y));
            else if (i == 3 && !IfDirectionLocked(Direction.RIGHT))
                tmpOrganism = getWorld().GetFieldOccupant(new Point(x + 1, y));

            if (tmpOrganism != null) {
                getWorld().DeleteOrganism(tmpOrganism);
                Messenger.AddMessage(OrganismToString() + " killed " + tmpOrganism.OrganismToString() + " with 'scorch' ability");
            }
        }
    }

    @Override
    protected Point PlanMove() {
        int x = getPosition().getX();
        int y = getPosition().getY();
        DrawRandomField(getPosition());
        if (movementDirection == Direction.NODIRECTION ||
                IfDirectionLocked(movementDirection)) return getPosition();
        else {
            if (movementDirection == Direction.DOWN) return new Point(x, y + 1);
            if (movementDirection == Direction.UP) return new Point(x, y - 1);
            if (movementDirection == Direction.LEFT) return new Point(x - 1, y);
            if (movementDirection == Direction.RIGHT) return new Point(x + 1, y);
            else return new Point(x, y);
        }
    }

    @Override
    public void Action() {
        if (ability.getIfActive()) {
            Messenger.AddMessage("'Scorch' ability is active (Rounds left: " + ability.getDuration() + ")");
            Scorch();
        }
        for (int i = 0; i < getMoveRange(); i++) {
            Point futurePosition = PlanMove();

            if (getWorld().IfFieldOccupied(futurePosition) && getWorld().GetFieldOccupant(futurePosition) != this)
            {
                Colision(getWorld().GetFieldOccupant(futurePosition));
                break;
            } else if (getWorld().GetFieldOccupant(futurePosition) != this){
                MakeMove(futurePosition);
            }
            if (ability.getIfActive()){
                Scorch();
            }
        }
        movementDirection = Direction.NODIRECTION;
        ability.CheckConditions();
    }

    public Ability getAbility() {
        return ability;
    }

    public void setMovementDirection(Direction movementDirection) {
        this.movementDirection = movementDirection;
    }
}