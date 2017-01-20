package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

import model.Board;
import sound.SoundPlayer;

/**
 * Creates and sets up the components needed for the Tetris game GUI.
 * @author Michael Lambion
 * @version 2016.05.28
 */
public class TetrisGUI extends Observable 
    implements ActionListener, Observer, PropertyChangeListener {
    
    /** The initial delay for the amount of time between each step of the game. */
    private static final int INITIAL_DELAY = 1000;
    
    /** Number of milliseconds to reduce from Timer delay on level up. */
    private static final int LEVEL_DELAY = 100;
    
    /** The default size for Tetris blocks at initialization. */
    private static final int DEFAULT_SIZE = 30;
    
    /** Title to display in the GUI frame. */
    private static final String GAME_TITLE = "Tetris";
    
    /** Default width for a Tetris board. */
    private static final int DEFAULT_WIDTH = 10;
    
    /** Default height for a Tetris board. */
    private static final int DEFAULT_HEIGHT = 20;
    
    /** File name to play Tetris theme music. */
    private static final String THEME_MUSIC = "sounds/tetris_theme.wav";
    
    /** JFrame to contain the GUI components for the Tetris game. */
    private final JFrame myFrame;

    /**  A timer used to run the Tetris game. */
    private final Timer myTimer;
    
    /** Board used to play Tetris. */
    private Board myBoard;
    
    /** Info panel to display controls, score, and next piece. */
    private TetrisInfoPanel myInfoPanel;
    
    /** Tetris Panel that represents the state of the game being played. */
    private TetrisPanel myTetrisPanel;
    
    /** Timer delay reduction for each level. */
    private int myTimerDelay;
    
    /** KeyListeners used to control the Tetris game. */
    private final TetrisKeyListener myKeyListeners;
    
    /** SoundPlayer to play WAV audio for music and sound effects. */
    private final SoundPlayer mySound = new SoundPlayer();
            
    /**
     * Constructs and sets the initial state of a 
     * Tetris GUI with given board size.
     */
    public TetrisGUI() {
        super();
        myFrame = new JFrame(GAME_TITLE);
        myFrame.getContentPane().setBackground(Color.BLUE);
        myTimer = new Timer(INITIAL_DELAY, this);
        myTimerDelay = LEVEL_DELAY;
        myKeyListeners = new TetrisKeyListener();
        try {
            mySound.preLoad(THEME_MUSIC);
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        }
        resizeBoard(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Sets the up the GUI components of the Tetris application.
     * Lays out panels and components needed in the main window.
     */
    public void start() {
        myFrame.getContentPane().setLayout(new BorderLayout());
        myFrame.getContentPane().add(myTetrisPanel, BorderLayout.WEST);
        myFrame.getContentPane().add(myInfoPanel, BorderLayout.CENTER);
        addObserver(myTetrisPanel);
        myTetrisPanel.setBorder(new LineBorder(Color.BLACK, 1));
        
        myFrame.pack();
        myFrame.setLocationRelativeTo(null);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
        myFrame.setResizable(false);      
    }
    
    /**
     * Resets the state of the game and starts a new game.
     */
    public void newGame() {
        myBoard.newGame();
        myInfoPanel.resetScore();
        mySound.loop(THEME_MUSIC);
        myTimer.setDelay(INITIAL_DELAY);
        myTimer.start();
        myFrame.addKeyListener(myKeyListeners);
        setChanged();
        notifyObservers(false);
    }
    
    /**
     * Ends the game and displays a dialog informing the user.
     */
    public void endGame() {
        myTimer.stop();
        try {
            mySound.stop(THEME_MUSIC);
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            mySound.play("sounds/game_over.wav");
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        }
        myFrame.removeKeyListener(myKeyListeners);
        JOptionPane.showMessageDialog(myFrame,
                        "Game over.");
        setChanged();
        notifyObservers(true);
    }
    
    /** 
     * Resizes the game board and re-initializes necessary components.
     * @param theWidth The width of the new game board. 
     * @param theHeight The height of the new game board.
     */
    public void resizeBoard(final int theWidth, final int theHeight) {
        myTimer.stop();
        mySound.stop(THEME_MUSIC);
        myFrame.removeKeyListener(myKeyListeners);

        myBoard = new Board(theWidth, theHeight);
        myBoard.addObserver(this);
        myTetrisPanel = new TetrisPanel(myBoard, DEFAULT_SIZE);
        myInfoPanel = new TetrisInfoPanel(myBoard);
        myInfoPanel.addPropertyChangeListener(this);
        myFrame.getContentPane().removeAll();
        myFrame.setJMenuBar(new TetrisMenuBar(this, myFrame));
        start();
    }

    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        final Object source = theEvent.getSource();
        if (source.equals(myTimer)) {
            myBoard.step();
            try {
                mySound.play("sounds/blip.wav");
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            }
        }  
    }
    
    @Override
    public void update(final Observable theO, final Object theArg) {
        if (theArg instanceof Boolean && (Boolean) theArg) {
            endGame();
        }
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if ("level".equals(theEvent.getPropertyName()) && myTimer.getDelay() > myTimerDelay) {
            myTimer.setDelay(myTimer.getDelay() - myTimerDelay);
        }
    }
    
    /** Changes the delay for difficulty. 
     * @param theDelay The number of milliseconds to reduce the timer delay by.
     */
    public void setDifficulty(final int theDelay) {
        myTimerDelay = theDelay;
    }
    
    /** Toggles the timer that runs the Tetris game.
     */
    public void togglePause() {
        if (myTimer.isRunning()) {
            myTimer.stop();
            try {
                mySound.pause(THEME_MUSIC);
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers(true);
        } else {
            myTimer.start();
            try {
                mySound.loop(THEME_MUSIC);
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers(false);
        }
    }
    
    /** Resizes the Tetris panel using the chosen size for the blocks. 
     *  @param theBlockSize The size of the blocks to be drawn in the panel. */
    public void resizePanel(final int theBlockSize) {
        myTetrisPanel.resizeBlocks(theBlockSize);
        start();
    }
    
    /**
     * Defines behavior for KeyEvents when interacting with the Tetris Board.
     * @author Michael Lambion
     * @version 2016.05.23
     */
    private class TetrisKeyListener extends KeyAdapter {
        /**
         * Boolean that checks whether the game is currently paused.
         */
        private boolean myIsActive = true;
        /**
         * Defines behavior for the Tetris Board depending on the key pressed.
         * @param theKey The KeyEvent being triggered.
         */
        public void keyPressed(final KeyEvent theKey) {
            if (theKey.getKeyCode() == KeyEvent.VK_P) {
                togglePause();
                toggleListeners();
            }
            if (myIsActive) {
                if (theKey.getKeyCode() == KeyEvent.VK_A) {
                    myBoard.left();
                } else if (theKey.getKeyCode() == KeyEvent.VK_D) {
                    myBoard.right();
                } else if (theKey.getKeyCode() == KeyEvent.VK_S) {
                    myBoard.down();
                } else if (theKey.getKeyCode() == KeyEvent.VK_Q) {
                    myBoard.rotateCW();
                } else if (theKey.getKeyCode() == KeyEvent.VK_E) {
                    myBoard.rotateCCW();
                } else if (theKey.getKeyCode() == KeyEvent.VK_SPACE) {
                    myBoard.drop();
                } 
            }
        }
        
        /**
         * Toggles the status of the KeyListeners.
         */
        public void toggleListeners() {
            myIsActive ^= true;
        }
       
    }
}
