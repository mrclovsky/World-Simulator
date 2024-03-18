package project;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class WorldGUI implements ActionListener, KeyListener {
    private Toolkit toolkit;
    private Dimension dimension;
    private JFrame jFrame;
    private JMenu menu;
    private JMenuItem newGame, load, save, exit;
    private BoardGraphics boardGraphics = null;
    private InformationPanel informationGraphics = null;
    private AboutPanel aboutGraphics = null;
    private Labels labels = null;
    private JPanel mainPanel;
    private final int INTERSPACE;
    private World world;

    public WorldGUI(String title) {
        toolkit = Toolkit.getDefaultToolkit();
        dimension = toolkit.getScreenSize();
        INTERSPACE = 12;
        jFrame = new JFrame(title);
        jFrame.setBounds(dimension.width/3, dimension.height/4,1200, 720);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        newGame = new JMenuItem("New game");
        load = new JMenuItem("Load");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");
        newGame.addActionListener(this);
        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);
        menu.add(newGame);
        menu.add(load);
        menu.add(save);
        menu.add(exit);
        menuBar.add(menu);
        jFrame.setJMenuBar(menuBar);
        jFrame.setLayout(new CardLayout());

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.GRAY);
        mainPanel.setLayout(null);

        jFrame.addKeyListener(this);
        jFrame.add(mainPanel);
        jFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == newGame) {
            Messenger.DeleteMessages();
            int sizeX = Integer.parseInt(JOptionPane.showInputDialog(jFrame, "Enter width:", "15"));
            int sizeY = Integer.parseInt(JOptionPane.showInputDialog(jFrame, "Enter height:", "15"));
            double worldFillPercentage = Double.parseDouble(JOptionPane.showInputDialog(jFrame, "Enter world fill percentage:", "60"));
            double worldFill = worldFillPercentage/100;

            world = new World(sizeX, sizeY, this);
            world.GenerateWorld(worldFill);
            if (boardGraphics != null)
                mainPanel.remove(boardGraphics);
            if (informationGraphics != null)
                mainPanel.remove(informationGraphics);
            if (aboutGraphics != null)
                mainPanel.remove(aboutGraphics);
            if (labels != null)
                mainPanel.remove(labels);
            StartGame();
        }
        if (event.getSource() == load) {
            Messenger.DeleteMessages();
            String nameOfFile = JOptionPane.showInputDialog(jFrame, "Enter filename:", "");
            world = World.LoadWorld(nameOfFile);
            world.setWorldGUI(this);
            boardGraphics = new BoardGraphics(world);
            informationGraphics = new InformationPanel();
            aboutGraphics = new AboutPanel();
            labels = new Labels();
            if (boardGraphics != null)
                mainPanel.remove(boardGraphics);
            if (informationGraphics != null)
                mainPanel.remove(informationGraphics);
            if (aboutGraphics != null)
                mainPanel.remove(aboutGraphics);
            if (labels != null)
                mainPanel.remove(labels);
            StartGame();
        }
        if (event.getSource() == save) {
            String nameOfFile = JOptionPane.showInputDialog(jFrame, "Enter filename:", "");
            world.SaveWorld(nameOfFile);
            Messenger.AddMessage("World saved");
            informationGraphics.RefreshMessages();
        }
        if (event.getSource() == exit) {
            jFrame.dispose();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (world != null && world.isPause()) {
            int keyCode = event.getKeyCode();
            if (keyCode == KeyEvent.VK_ENTER) {

            } else if (world.getIfHuman()) {
                if (keyCode == KeyEvent.VK_UP) {
                    world.getHuman().setMovementDirection(Organism.Direction.UP);
                } else if (keyCode == KeyEvent.VK_DOWN) {
                    world.getHuman().setMovementDirection(Organism.Direction.DOWN);
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    world.getHuman().setMovementDirection(Organism.Direction.LEFT);
                } else if (keyCode == KeyEvent.VK_RIGHT) {
                    world.getHuman().setMovementDirection(Organism.Direction.RIGHT);
                } else if (keyCode == KeyEvent.VK_V) {
                    Ability tmpAbility = world.getHuman().getAbility();
                    if (tmpAbility.getIfCanActivate()) {
                        tmpAbility.Activate();
                        Messenger.AddMessage("Ability 'scorch' has been activated (Rounds left: " + tmpAbility.getDuration() + ")");

                    } else if (tmpAbility.getIfActive()) {
                        Messenger.AddMessage("Ability 'scorch' already active (Rounds left: " + tmpAbility.getDuration() + ")");
                        informationGraphics.RefreshMessages();
                        return;
                    } else {
                        Messenger.AddMessage("Ability 'scorch' can be activated again after " + tmpAbility.getCooldown() + " rounds");
                        informationGraphics.RefreshMessages();
                        return;
                    }
                } else {
                    Messenger.AddMessage("Unknown key");
                    informationGraphics.RefreshMessages();
                    return;
                }
            } else if (!world.getIfHuman() && (keyCode == KeyEvent.VK_UP ||
                    keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT ||
                    keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_V)) {
                Messenger.AddMessage("Human is dead...");
                informationGraphics.RefreshMessages();
                return;
            } else {
                Messenger.AddMessage("Unknown key");
                informationGraphics.RefreshMessages();
                return;
            }
            Messenger.DeleteMessages();
            world.setPause(false);
            world.DoRound();
            RefreshWorld();
            world.setPause(true);
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    private class BoardGraphics extends JPanel {
        private final int sizeX;
        private final int sizeY;
        private BoardFields[][] boardFields;
        private World WORLD;

        public BoardGraphics(World world) {
            super();
            setBounds(mainPanel.getX() + INTERSPACE*4, mainPanel.getY() + INTERSPACE*2, mainPanel.getHeight()*4/5, mainPanel.getHeight()*4/5);
            WORLD = world;
            this.sizeX = world.getSizeX();
            this.sizeY = world.getSizeY();

            boardFields = new BoardFields[sizeY][sizeX];
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    boardFields[i][j] = new BoardFields(j, i);
                    boardFields[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent event) {
                            if (event.getSource() instanceof BoardFields) {
                                BoardFields tmpField = (BoardFields)event.getSource();
                                if (tmpField.isEmpty == true) {
                                    OrganismsList organismList = new OrganismsList(tmpField.getX() + jFrame.getX(), tmpField.getY() + jFrame.getY(), new Point(tmpField.getPositionX(), tmpField.getPositionY()));
                                }
                            }
                        }
                    });
                }
            }

            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    this.add(boardFields[i][j]);
                }
            }
            this.setLayout(new GridLayout(sizeY, sizeX));
        }

        private class BoardFields extends JButton {
            private boolean isEmpty;
            private Color fieldColor;
            private final int positionX;
            private final int positionY;

            public BoardFields(int X, int Y) {
                super();
                fieldColor = Color.WHITE;
                setBackground(fieldColor);
                setOpaque(true);
                isEmpty = true;
                positionX = X;
                positionY = Y;
            }

            public boolean isEmpty() {
                return isEmpty;
            }

            public void setEmpty(boolean empty) {
                isEmpty = empty;
            }


            public Color getFieldColor() {
                return fieldColor;
            }

            public void setFieldColor(Color fieldColor) {
                this.fieldColor = fieldColor;
                setBackground(fieldColor);
            }

            public int getPositionX() {
                return positionX;
            }

            public int getPositionY() {
                return positionY;
            }
        }

        public void RefreshBoard() {
            for (int i = 0; i < sizeY; i++) {
                for (int j = 0; j < sizeX; j++) {
                    Organism tmpOrganism = world.getBoard()[i][j];
                    if (tmpOrganism != null) {
                        boardFields[i][j].setEmpty(false);
                        boardFields[i][j].setEnabled(false);
                        boardFields[i][j].setFieldColor(tmpOrganism.getOrganismColor());
                    } else {
                        boardFields[i][j].setEmpty(true);
                        boardFields[i][j].setEnabled(true);
                        boardFields[i][j].setFieldColor(Color.WHITE);
                    }
                }
            }
        }

        public int getSizeX() {
            return sizeX;
        }

        public int getSizeY() {
            return sizeY;
        }

        public BoardFields[][] getBoardFields() {
            return boardFields;
        }
    }

    private class InformationPanel extends JPanel {
        private String text;
        private final String about = "Messages:\n";
        private JTextArea textArea;

        public InformationPanel() {
            super();
            setBounds(boardGraphics.getX() + boardGraphics.getWidth() + INTERSPACE*2, mainPanel.getY() + INTERSPACE*14, mainPanel.getWidth() - boardGraphics.getWidth() - INTERSPACE*10, mainPanel.getHeight()*4/6 - INTERSPACE*4 );
            text = Messenger.getMessage();
            textArea = new JTextArea(text);
            textArea.setEditable(false);
            setLayout(new CardLayout());

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setMargin(new Insets(10, 10, 10, 10));
            JScrollPane panel = new JScrollPane(textArea);
            add(panel);
        }

        public void RefreshMessages() {
            text = about + Messenger.getMessage();
            textArea.setText(text);
        }
    }

    private class AboutPanel extends JPanel {
        private String text;
        private final String about = "Author: Marceli Grad s188724\n\nControls:\n<^> - movement\n" + "V - ability 'scorch'\nEnter - next round\n";
        private JTextArea textArea;

        public AboutPanel() {
            super();
            setBounds(boardGraphics.getX() + boardGraphics.getWidth() + INTERSPACE*2, mainPanel.getY() + INTERSPACE*2, mainPanel.getWidth() - boardGraphics.getWidth() - INTERSPACE*10, mainPanel.getHeight()/4 - INTERSPACE*2 );
            textArea = new JTextArea(text);
            textArea.setEditable(false);
            setLayout(new CardLayout());

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setMargin(new Insets(10, 10, 10, 10));
            JScrollPane panel = new JScrollPane(textArea);
            add(panel);
        }

        public void RefreshMessages() {
            text = about;
            textArea.setText(text);
        }

    }

    private class OrganismsList extends JFrame {
        private String[] organismsList;
        private Organism.OrganismType[] oragnismTypeList;
        private JList jList;

        public OrganismsList(int x, int y, Point point) {
            super("Choose organism");
            setBounds(x, y, 280, 260);
            organismsList = new String[]{"Fox", "Wolf", "Antelope", "Turtle", "Sheep", "Cyber sheep", "Grass", "Dandelion", "Guarana", "Nightshade", "Sosnowsky Hogweed"};
            oragnismTypeList = new Organism.OrganismType[]{
                    Organism.OrganismType.FOX,
                    Organism.OrganismType.WOLF,
                    Organism.OrganismType.ANTELOPE,
                    Organism.OrganismType.TURTLE,
                    Organism.OrganismType.SHEEP,
                    Organism.OrganismType.CYBERSHEEP,
                    Organism.OrganismType.GRASS,
                    Organism.OrganismType.DANDELION,
                    Organism.OrganismType.GUARANA,
                    Organism.OrganismType.NIGHTSHADE,
                    Organism.OrganismType.SOSNOWSKYHOGWEED
            };

            jList = new JList(organismsList);
            jList.setVisibleRowCount(organismsList.length);
            jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent event) {
                    Organism tmpOrganism = OrganismFactory.CreateOrganism(oragnismTypeList[jList.getSelectedIndex()], world, point);
                    world.AddOrganism(tmpOrganism);
                    Messenger.AddMessage("New organism created - " + tmpOrganism.OrganismToString());
                    RefreshWorld();
                    dispose();

                }
            });

            JScrollPane panel = new JScrollPane(jList);
            add(panel);
            setVisible(true);
        }
    }

    private class Labels extends JPanel {
        private final int NUMBER_OF_DIFFERENT_SPECIES = 12;
        private JButton[] jButtons;

        public Labels() {
            super();
            setBounds(mainPanel.getX(), mainPanel.getHeight() - INTERSPACE*7, mainPanel.getWidth(), mainPanel.getHeight());
            setBackground(Color.GRAY);
            setLayout(new FlowLayout(FlowLayout.CENTER));
            jButtons = new JButton[NUMBER_OF_DIFFERENT_SPECIES];

            jButtons[0] = new JButton("HUMAN");
            jButtons[0].setBackground(Color.BLUE);
            jButtons[0].setOpaque(true);

            jButtons[1] = new JButton("FOX");
            jButtons[1].setBackground(new Color(255, 125, 0));
            jButtons[1].setOpaque(true);

            jButtons[2] = new JButton("WOLF");
            jButtons[2].setBackground(Color.LIGHT_GRAY);
            jButtons[2].setOpaque(true);

            jButtons[3] = new JButton("ANTELOPE");
            jButtons[3].setBackground(new Color(150, 70, 0));
            jButtons[3].setOpaque(true);

            jButtons[4] = new JButton("TURTLE");
            jButtons[4].setBackground(new Color(0, 100, 0));
            jButtons[4].setOpaque(true);

            jButtons[5] = new JButton("SHEEP");
            jButtons[5].setBackground(Color.PINK);
            jButtons[5].setOpaque(true);

            jButtons[6] = new JButton("CYBER SHEEP");
            jButtons[6].setBackground(Color.black);
            jButtons[6].setOpaque(true);

            jButtons[7] = new JButton("GRASS");
            jButtons[7].setBackground(Color.GREEN);
            jButtons[7].setOpaque(true);

            jButtons[8] = new JButton("DANDELION");
            jButtons[8].setBackground(Color.YELLOW);
            jButtons[8].setOpaque(true);

            jButtons[9] = new JButton("GUARANA");
            jButtons[9].setBackground(Color.RED);
            jButtons[9].setOpaque(true);

            jButtons[10] = new JButton("NIGHTSHADE");
            jButtons[10].setBackground(new Color(20, 20, 80));
            jButtons[10].setOpaque(true);

            jButtons[11] = new JButton("SOSNOWSKY HOGWEED");
            jButtons[11].setBackground(Color.CYAN);
            jButtons[11].setOpaque(true);

            for (int i = 0; i < NUMBER_OF_DIFFERENT_SPECIES; i++) {
                jButtons[i].setEnabled(true);
                add(jButtons[i]);
            }

        }
    }

    private void StartGame() {
        boardGraphics = new BoardGraphics(world);
        mainPanel.add(boardGraphics);

        informationGraphics = new InformationPanel();
        mainPanel.add(informationGraphics);

        aboutGraphics = new AboutPanel();
        mainPanel.add(aboutGraphics);

        labels = new Labels();
        mainPanel.add(labels);

        RefreshWorld();
    }

    public void RefreshWorld() {
        boardGraphics.RefreshBoard();
        informationGraphics.RefreshMessages();
        aboutGraphics.RefreshMessages();
        SwingUtilities.updateComponentTreeUI(jFrame);
        jFrame.requestFocusInWindow();
    }

    public World getWorld() {
        return world;
    }

    public BoardGraphics getBoardGraphics() {
        return boardGraphics;
    }

    public InformationPanel getInformationGraphics() {
        return informationGraphics;
    }

    public AboutPanel getAboutGraphics() {
        return aboutGraphics;
    }
}
