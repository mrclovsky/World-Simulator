package project;
import project.animals.Human;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;

public class World {
    private int sizeX;
    private int sizeY;
    private int round;
    private Organism[][] board;
    private boolean ifHuman;
    private boolean ifEnd;
    private boolean pause;
    private ArrayList<Organism> organisms;
    private Human human;
    private WorldGUI worldGUI;

    public World(WorldGUI worldGUI) {
        this.sizeX = 0;
        this.sizeY = 0;
        round = 0;
        ifHuman = true;
        ifEnd = false;
        pause = true;
        organisms = new ArrayList<>();
        this.worldGUI = worldGUI;
    }

    public World(int sizeX, int sizeY, WorldGUI worldGUI) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        round = 0;
        ifHuman = true;
        ifEnd = false;
        pause = true;
        board = new Organism[sizeY][sizeX];
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                board[i][j] = null;
            }
        }
        organisms = new ArrayList<>();
        this.worldGUI = worldGUI;
    }


    public void SaveWorld(String fileName) {
        try {
            fileName += ".txt";
            File file = new File(fileName);
            file.createNewFile();

            PrintWriter pw = new PrintWriter(file);
            pw.print(sizeX + " ");
            pw.print(sizeY + " ");
            pw.print(round + " ");
            pw.print(ifHuman + " ");
            pw.print(ifEnd + "\n");
            for (int i = 0; i < organisms.size(); i++) {
                pw.print(organisms.get(i).getOrganismType() + " ");
                pw.print(organisms.get(i).getPosition().getX() + " ");
                pw.print(organisms.get(i).getPosition().getY() + " ");
                pw.print(organisms.get(i).getStrength() + " ");
                pw.print(organisms.get(i).getBirthRound() + " ");
                pw.print(organisms.get(i).getIfDead());
                if (organisms.get(i).getOrganismType() == Organism.OrganismType.HUMAN) {
                    pw.print(" " + human.getAbility().getDuration() + " ");
                    pw.print(human.getAbility().getCooldown() + " ");
                    pw.print(human.getAbility().getIfActive() + " ");
                    pw.print(human.getAbility().getIfCanActivate());
                }
                pw.println();
            }
            pw.close();
        } catch (IOException exc) {
            System.out.println("Error: " + exc);
        }
    }

    public static World LoadWorld(String fileName) {
        try {
            fileName += ".txt";
            File file = new File(fileName);

            Scanner scanner = new Scanner(file);
            String line = scanner.nextLine();
            String[] properties = line.split(" ");
            int sizeX = Integer.parseInt(properties[0]);
            int sizeY = Integer.parseInt(properties[1]);
            World tmpWorld = new World(sizeX, sizeY, null);
            int round = Integer.parseInt(properties[2]);
            tmpWorld.round = round;
            boolean ifHuman = Boolean.parseBoolean(properties[3]);
            tmpWorld.ifHuman = ifHuman;
            boolean ifEnd = Boolean.parseBoolean(properties[4]);
            tmpWorld.ifEnd = ifEnd;
            tmpWorld.human = null;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                properties = line.split(" ");
                Organism.OrganismType organismType = Organism.OrganismType.valueOf(properties[0]);
                int x = Integer.parseInt(properties[1]);
                int y = Integer.parseInt(properties[2]);

                Organism tmpOrganism = OrganismFactory.CreateOrganism(organismType, tmpWorld, new Point(x, y));
                int strength = Integer.parseInt(properties[3]);
                tmpOrganism.setStrength(strength);
                int birthRound = Integer.parseInt(properties[4]);
                tmpOrganism.setBirthRound(birthRound);
                boolean ifDead = Boolean.parseBoolean(properties[5]);
                tmpOrganism.setIfDead(ifDead);

                if (organismType == Organism.OrganismType.HUMAN) {
                    tmpWorld.human = (Human)tmpOrganism;
                    int duration = Integer.parseInt(properties[6]);
                    tmpWorld.human.getAbility().setDuration(duration);
                    int cooldown = Integer.parseInt(properties[7]);
                    tmpWorld.human.getAbility().setCooldown(cooldown);
                    boolean ifActive = Boolean.parseBoolean(properties[8]);
                    tmpWorld.human.getAbility().setIfActive(ifActive);
                    boolean ifCanActivate = Boolean.parseBoolean(properties[9]);
                    tmpWorld.human.getAbility().setIfCanActivate(ifCanActivate);
                }
                tmpWorld.AddOrganism(tmpOrganism);
            }
            scanner.close();
            return tmpWorld;
        } catch (IOException exc) {
            System.out.println("Error: " + exc);
        }
        return null;
    }

    public void GenerateWorld(double worldFill) {
        int organismsNumber = (int) Math.floor(sizeX * sizeY * worldFill);
        Point position = DrawFreeField();
        Organism tmpOrganism = OrganismFactory.CreateOrganism(Organism.OrganismType.HUMAN, this, position);
        AddOrganism(tmpOrganism);
        human = (Human)tmpOrganism;

        for (int i = 0; i < organismsNumber - 1; i++) {
            position = DrawFreeField();
            if (position != new Point(-1, -1)) {
                AddOrganism(OrganismFactory.CreateOrganism(Organism.DrawOrganismType(), this, position));
            } else return;
        }
    }

    public void DoRound() {
        if (ifEnd) return;
        round++;
        Messenger.AddMessage("\nROUND: " + round);
        SortOrganisms();
        for (int i = 0; i < organisms.size(); i++) {
            if (organisms.get(i).getBirthRound() != round
                    && organisms.get(i).getIfDead() == false) {
                organisms.get(i).Action();
            }
        }
        for (int i = 0; i < organisms.size(); i++) {
            if (organisms.get(i).getIfDead() == true) {
                organisms.remove(i);
                i--;
            }
        }
        for (int i = 0; i < organisms.size(); i++) {
            organisms.get(i).setIfBreeded(false);
        }
    }

    private void SortOrganisms() {
        Collections.sort(organisms, new Comparator<Organism>() {
            @Override
            public int compare(Organism org1, Organism org2) {
                if (org1.getInitiative() != org2.getInitiative())
                    return Integer.valueOf(org2.getInitiative()).compareTo(org1.getInitiative());
                else
                    return Integer.valueOf(org1.getBirthRound()).compareTo(org2.getBirthRound());
            }
        });
    }

    public Point DrawFreeField() {
        Random rnd = new Random();
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                if (board[i][j] == null) {
                    while (true) {
                        int x = rnd.nextInt(sizeX);
                        int y = rnd.nextInt(sizeY);
                        if (board[y][x] == null) return new Point(x, y);
                    }
                }
            }
        }
        return new Point(-1, -1);
    }

    public boolean IfFieldOccupied(Point field) {
        if (board[field.getY()][field.getX()] == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public Organism GetFieldOccupant(Point field) {
        return board[field.getY()][field.getX()];
    }

    public void AddOrganism(Organism organism) {
        organisms.add(organism);
        board[organism.getPosition().getY()][organism.getPosition().getX()] = organism;
    }

    public void DeleteOrganism(Organism organism) {
        board[organism.getPosition().getY()][organism.getPosition().getX()] = null;
        organism.setIfDead(true);
        if (organism.getOrganismType() == Organism.OrganismType.HUMAN) {
            ifHuman = false;
            human = null;
        }
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getRound() {
        return round;
    }

    public Organism[][] getBoard() {
        return board;
    }

    public boolean getIfHuman() {
        return ifHuman;
    }

    public boolean getIfEnd() {
        return ifEnd;
    }

    public ArrayList<Organism> getOrganisms() {
        return organisms;
    }

    public Human getHuman() {
        return human;
    }

    public void setHuman(Human human) {
        this.human = human;
    }

    public void setIfHuman(boolean ifHuman) {
        this.ifHuman = ifHuman;
    }

    public void setIfEnd(boolean ifEnd) {
        this.ifEnd = ifEnd;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public WorldGUI getWorldGUI() {
        return worldGUI;
    }

    public void setWorldGUI(WorldGUI worldGUI) {
        this.worldGUI = worldGUI;
    }

    public boolean ifHogweedExists() {
        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                if (board[i][j] != null &&
                        board[i][j].getOrganismType() == Organism.OrganismType.SOSNOWSKYHOGWEED) {
                    return true;
                }
            }
        }
        return false;
    }
}

