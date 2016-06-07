/*
 * TCSS 305 Assignment 6 - Tetris
 */

package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Board;
import model.Piece;
import sound.Sound;

/**
 * The Tetris GUI.
 *
 * @author Tzu-Chien Lu
 * @version Dec 11 2015
 */
public class GUI implements Observer {

    // Constants
    
    /** Game panel width as assigned. */
    private static final int GAMEPANEL_WIDTH = 10;

    /** Game panel height as assigned. */
    private static final int GAMEPANEL_HEIGHT = 20;
    
    /** The width of east panel. */
    private static final int ESAT_PANEL_WIDTH = 100;

    /** The height of east panel. */
    private static final int EAST_PANEL_HEIGHT = 150;

    /** The space white space between boxes. */
    private static final int PADDING = 20;

    /** The dimension of the frame. */
    private static final Dimension FRAME_SIZE = new Dimension(600, 800);

    /** Default delay between Timer events 1000 milliseconds = 1 second. */
    private static final int TIMER_DELAY = 1000;

    /** The block size. */
    private static final int BLOCK_SIZE = 25;

    /** The image file root. */
    private static final String IMAGE = "images/tetris.jpg";

    // Instance Fields
    
    /** A JFrame is used to display. */
    private JFrame myFrame;

    /** A Board is used to display. */
    private Board myBoard;
    
    /** A Array Deque is used to contain all pieces. */
    private Deque<Piece> myPieces;
    
    /** A JPanel is used to display game. */
    private GamePanel myGamePanel;

    /** A JPanel displays next piece. */
    private NextPiecePanel myNextPiecePanel;

    /** A JPanel displays score. */
    private ScorePanel myScorePanel;

    /** A JPanel displays key information. */
    private InfoPanel myInfoPanel;
    
    /** A boolean represents my game is over. */
    private Boolean myGameOver;

    /** A Timer is used to set up delay to call listener. */
    private Timer myTimer;

    /** JMenuItem indicate the new game. */
    private JMenuItem myNewGame;
    
    /** An inner class for accelerated key control. */
    private MyKeyAdapter myKeyControl;
        
    /** A boolean represents if the game is paused. */
    private boolean myGamePaused;
    
    /**
     * Constructor, although everything is initialized in helper method.
     */
    public GUI() {
        setup();
    }
    
    /**
     * A helper method for constructor.
     */
    private void setup() {
        myFrame = new JFrame("Tetris");
        
        /** Setup the board with width and height. */
        myBoard = new Board(GAMEPANEL_WIDTH, GAMEPANEL_HEIGHT);
        myPieces = new ArrayDeque<Piece>();
        myGamePanel = new GamePanel(myBoard);
        myTimer = new Timer(TIMER_DELAY, new TimerListener(myBoard));
        myScorePanel = new ScorePanel(myBoard, myTimer, TIMER_DELAY);
        myInfoPanel = new InfoPanel();
        myNextPiecePanel = new NextPiecePanel(myBoard);
        myGameOver = false;
        
        /** Add this class to observer list. */
        myBoard.addObserver(this);
        myGamePaused = false;
        
        /** A KeyAdapter for key control. */
        myKeyControl = new MyKeyAdapter();
    }

    /**
     *  Sets up and displays the GUI for this application.
     */
    public void start() {
        myFrame.setJMenuBar(createMenuBar());
        setupEast();
        setupCenter();
        
        /** Set icon of JMenuBar. */
        Image picture = null;
        try {
            picture = ImageIO.read(new File(IMAGE));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        myFrame.setIconImage(picture);
        
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(FRAME_SIZE);
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
        myFrame.setResizable(false);
        myFrame.pack();        
    }

    /**
     * Set up the center panel for game play.
     */
    private void setupCenter() {
        final JPanel centerPanel = new JPanel();
        centerPanel.setBackground(Color.BLACK);
        myGamePanel.setBackground(Color.WHITE);
        myGamePanel.setPreferredSize(new Dimension(GAMEPANEL_WIDTH * BLOCK_SIZE, 
                                                   GAMEPANEL_HEIGHT * BLOCK_SIZE));
        centerPanel.add(myGamePanel);
        myFrame.add(centerPanel);
    }

    /**
     * Set up the east panel for display next piece, information, and score.
     */
    public void setupEast() {
        
        /**set up the East panel. */
        final JPanel eastPanel = new JPanel();
        
        /** Use BoxLayout to keep 3 panels to fit the JPanel. */
        final BoxLayout b = new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS);
        eastPanel.setLayout(b);
        eastPanel.setBackground(Color.BLACK);

        /** Inner panel 1 for next piece. */
        myNextPiecePanel.setPreferredSize(new Dimension(ESAT_PANEL_WIDTH, EAST_PANEL_HEIGHT));

        /** Inner panel 2 for score. */
        myScorePanel.setPreferredSize(new Dimension(ESAT_PANEL_WIDTH * 2, EAST_PANEL_HEIGHT));

        /** Inner panel 3 for information. */
        myInfoPanel.setPreferredSize(new Dimension(ESAT_PANEL_WIDTH, EAST_PANEL_HEIGHT));

        /** Use BoxLayout to keep 3 panels in same vertical alignment. */
        final Box eastBox = new Box(BoxLayout.PAGE_AXIS);
        eastBox.add(Box.createGlue());
        eastBox.add(myNextPiecePanel);
        eastBox.add(Box.createVerticalStrut(PADDING));
        eastBox.add(myScorePanel);
        eastBox.add(Box.createGlue());
        eastBox.add(Box.createVerticalStrut(PADDING));
        eastBox.add(myInfoPanel);

        /** Add the box to a panel of BoxLayout. */
        eastPanel.add(eastBox);
        myFrame.add(eastPanel, BorderLayout.EAST);
    }

    /**
     * Create the Menu bar.
     * 
     * @return menuBar JMenuBar.
     */
    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        final JMenu[] menuArray = {createOptionMenu(), createSettingMenu(), createHelpMenu()};
        
        for (final JMenu m : menuArray) {
            menuBar.add(m);
        }
        return menuBar;
    }

    /**
     * Create option menu.
     * 
     * @return optionMenu.
     */
    private JMenu createOptionMenu() {
        final JMenu optionMenu = new JMenu("Option");
        optionMenu.setMnemonic(KeyEvent.VK_O);
        
        createJMenuItemNewGame(optionMenu);
        createJMenuItemEndGame(optionMenu);
        createJMenuItemTurnOffSound(optionMenu);
        createJMenuItemExit(optionMenu);
        return optionMenu;
    }

    /**
     * Create JMenuItem for new game.
     * 
     * @param theMenu JMenu
     */
    private void createJMenuItemNewGame(final JMenu theMenu) {
        myNewGame = new JMenuItem("New Game", (int) 'N');
        myNewGame.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                newGame();
                myScorePanel.reset();
                myNewGame.setEnabled(false);
            }
        });
        theMenu.add(myNewGame);
        theMenu.addSeparator();
    }
    
    /**
     * Create JMenuItem for game over.
     * 
     * @param theMenu JMenu
     */
    private void createJMenuItemEndGame(final JMenu theMenu) {
        final JMenuItem endGame = new JMenuItem("End Game", (int) 'E');
        endGame.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                endGame();
                myNewGame.setEnabled(true);
            }
        });
        theMenu.add(endGame);
        theMenu.addSeparator();
    }
    
    /**
     * Create JMenuItem for turn of music.
     * 
     * @param theMenu JMenu.
     */
    private void createJMenuItemTurnOffSound(final JMenu theMenu) {
        final JMenuItem turnOff = new JMenuItem("Turn off Music", (int) 'T');
        turnOff.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                Sound.GAME.stop();
            }
        });
        theMenu.add(turnOff);
        theMenu.addSeparator();
    }
    
    /**
     * Create JMenuItem for exit.
     * 
     * @param theMenu JMenu
     */
    private void createJMenuItemExit(final JMenu theMenu) {
        final JMenuItem exit = new JMenuItem("Exit", (int) 'X');
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
//                myFrame.dispatchEvent(new WindowEvent(myFrame, WindowEvent.WINDOW_CLOSING));
                myFrame.dispose();
            }
        });
        theMenu.add(exit);
    }

    /**
     * Start a new game.
     */
    private void newGame() {
        myTimer.start();
        myGamePanel.addNotify();
        myBoard.newGame(GAMEPANEL_WIDTH, GAMEPANEL_HEIGHT, myPieces);
        myNewGame.setEnabled(false);
        myGamePaused = true;
        myFrame.addKeyListener(myKeyControl);
        myScorePanel.reset();
        
        /** Play the music. */
        Sound.GAME.play();
    }
    
    /**
     * End the current game.
     */
    private void endGame() {
        myTimer.stop();
        myNewGame.setEnabled(true);
        
        /** Stop the music. */
        Sound.GAME.stop();
        Sound.DROP.stop();
        Sound.MOVE.stop();
        
        /** We want the key control disable when end game. */
        myFrame.removeKeyListener(myKeyControl);
        JOptionPane.showMessageDialog(myFrame, "Game Over!");
    }
    
    /**
     * Pause or resume game.
     */
    private void pauseGame() {
        
        if (myGamePaused) {
            myGamePaused = !myGamePaused;
            myTimer.stop();
            Sound.GAME.stop();
            myGamePanel.pauseAndResume();
        
        } else {
            myGamePaused = !myGamePaused;
            myTimer.start();
            Sound.GAME.play();
            myGamePanel.pauseAndResume();
        }
    }

    /**
     * Create Setting menu.
     * 
     * @return setting menu.
     */
    private JMenu createSettingMenu() {
        final JMenu setting = new JMenu("Setting");
        setting.setMnemonic(KeyEvent.VK_S);
        gridCheckBox(setting);   
        return setting;
    }
    
    /**
     * Helper method for JCheckBoxMenuItem.
     * 
     * @param theMenu JMenu
     */
    private void gridCheckBox(final JMenu theMenu) {
        final JCheckBoxMenuItem grid = new JCheckBoxMenuItem("Grid");
        grid.setMnemonic(KeyEvent.VK_G);
        grid.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                
                if (myGamePanel.isGridChecked()) {
                    myGamePanel.gridCheck(false);
                    
                } else {
                    myGamePanel.gridCheck(true);
                }
            }
        });
        theMenu.add(grid);
    }

    /**
     * Create Help menu.
     * 
     * @return helpMenu.
     */
    private JMenu createHelpMenu() {
        final JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        final JMenuItem help = new JMenuItem("About...", (int) 'A');
        help.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                aboutInfor();
            }
        });
        final JMenuItem scores = new JMenuItem("Scores...", (int) 'S');
        scores.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                scores();
            }
        });
        helpMenu.add(scores);
        helpMenu.add(help);
        return helpMenu;
    }

    /**
     * Helper method of Game rule.
     */
    private void scores() {
        myTimer.stop();
        myGamePaused = false;
        ImageIcon icon = new ImageIcon(IMAGE);
        final Image img = icon.getImage();
        final Image newimg = img.getScaledInstance(480, 360, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);
        final int input = JOptionPane.showOptionDialog(null,                  
                                               "1. Complete 1 line scores 20 points.\n"
                                               + "2. Level up when 3 lines are completed.\n"
                                               + "3. Level up points = level * 100.\n"
                                               + "4. Speed increases when level up.", 
                                               "Scores Rule", JOptionPane.OK_CANCEL_OPTION,
                                     JOptionPane.INFORMATION_MESSAGE, icon, null, null);
        if (input == JOptionPane.OK_OPTION || input == JOptionPane.CLOSED_OPTION
                        || input == JOptionPane.CANCEL_OPTION) {
            myTimer.start();
            myGamePaused = true;
        }
    }
    
    /**
     * Helper method for about information.
     */
    private void aboutInfor() {
        myTimer.stop();
        myGamePaused = false;
    
        ImageIcon icon = new ImageIcon(IMAGE);
        final Image img = icon.getImage();
        final Image newimg = img.getScaledInstance(480, 360, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);
        final int input = JOptionPane.showOptionDialog(null,
                                               "Tetris credits:\n"
                                               + "Images: http://goo.gl/00TvL6\n"
                                               + "Sound implement: "
                                               + "http://goo.gl/WQYRG\n"
                                               + "Sound source: https://goo.gl/0bRGpr\n"
                                               + "YouTube to MP3: "
                                               + "http://www.youtube-mp3.org/\n"
                                               + "MP3 to WAV: http://goo.gl/BFTFdq\n"
                                               + "Other tool: Google URL Shortener.",
                                               "About", JOptionPane.OK_CANCEL_OPTION,
                                           JOptionPane.INFORMATION_MESSAGE, icon, null, null);
        
        if (input == JOptionPane.OK_OPTION || input == JOptionPane.CLOSED_OPTION
                        || input == JOptionPane.CANCEL_OPTION) {
            myTimer.start();
            myGamePaused = true;
        }
    }
    
    @Override
    public void update(final Observable theObj, final Object theArg) {
        
        /** When game over from Board class. */
        if (theArg instanceof Boolean) {
            myGamePanel.removeNotify();
            endGame();
            myGamePaused = false;
        }
        
        myGameOver = ((Board) theObj).isGameOver();
        
        if (myGameOver) {
            endGame();
        }
    }

    /**
     * Listens for mouse events relating to this panel.
     * 
     * @author Tzu-Chien Lu
     * @version Dec 11 2015
     */
    private class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent theEvent) {
            final int code = theEvent.getKeyCode();
            
            /** When pause, only 'P' is working. */
            if (theEvent.getKeyCode() == KeyEvent.VK_P) {
                pauseGame();
            }
                
            /** All accelerate keys work. */
            if (myGamePaused) {
                switch (code) {
                    case KeyEvent.VK_UP:
                        myBoard.rotate();
                        Sound.MOVE.play();
                        break;      
                    case KeyEvent.VK_DOWN:
                        myBoard.moveDown();
                        Sound.MOVE.play();
                        break;
                    case KeyEvent.VK_LEFT:
                        myBoard.moveLeft();
                        Sound.MOVE.play();
                        break;
                    case KeyEvent.VK_RIGHT:
                        myBoard.moveRight();
                        Sound.MOVE.play();
                        break;
                    case KeyEvent.VK_SPACE:
                        myBoard.hardDrop();
                        Sound.DROP.play();
                        break;
                    default:
                        break;
                }              
            }
        }
    }

    /**
     * Inner class for TimerListener.
     * 
     * @author Tzu-Chien Lu
     * @version Dec 11 2015
     */
    public final class TimerListener implements ActionListener {

        /** The Tetris Board that this Timer represents. */
        private final Board myBoard;

        /**
         * Constructor for this TimerListener.
         *
         * @param theBoard where timer is attached.
         */
        public TimerListener(final Board theBoard) {
            myBoard = theBoard;
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myBoard.step();
        }
    }
}
