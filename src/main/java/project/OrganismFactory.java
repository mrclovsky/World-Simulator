package project;
import project.plants.*;
import project.animals.*;


public class OrganismFactory {
    public static Organism CreateOrganism(Organism.OrganismType organismType, World world, Point position) {
        switch (organismType) {
            case WOLF:
                return new Wolf(world, position, world.getRound());
            case SHEEP:
                return new Sheep(world, position, world.getRound());
            case FOX:
                return new Fox(world, position, world.getRound());
            case TURTLE:
                return new Turtle(world, position, world.getRound());
            case ANTELOPE:
                return new Antelope(world, position, world.getRound());
            case HUMAN:
                return new Human(world, position, world.getRound());
            case GRASS:
                return new Grass(world, position, world.getRound());
            case DANDELION:
                return new Dandelion(world, position, world.getRound());
            case GUARANA:
                return new Guarana(world, position, world.getRound());
            case NIGHTSHADE:
                return new Nightshade(world, position, world.getRound());
            case SOSNOWSKYHOGWEED:
                return new SosnowskyHogweed(world, position, world.getRound());
            case CYBERSHEEP:
                return new CyberSheep(world, position, world.getRound());
            default:
                return null;
        }
    }
}