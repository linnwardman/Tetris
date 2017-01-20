package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Board;
import model.TetrisPiece;
import sound.SoundPlayer;

/**
 * Creates the information panel for the Tetris GUI, which contains
 * the next piece to be played, the controls for the game, and the
 * user's current score and level.
 * @author Michael Lambion
 * @version 2016.05.28
 */
public class TetrisInfoPanel extends JPanel implements Observer {
    
    /**Generated serial version UID. */
    private static final long serialVersionUID = 6188969562532570911L;

    /** Defines the size of the components in the JPanel. */
    private static final Dimension COMPONENT_SIZE = new Dimension(180, 180);
    
    /** Defines the width of the info panel. */
    private static final Dimension PANEL_SIZE = new Dimension(200, 300);
    
    /** Defines the amount of space between components of the info panel. */
    private static final Dimension PANEL_SPACE = new Dimension(0, 15);
    
    /** Number of lines to be cleared to advance one level. */
    private static final int LINES_NEXT_LEVEL = 5;
    
    /** Number of lines to clear to get a Tetris. */
    private static final int TETRIS_LINES = 4;
    
    /** Multiplier for clearing a Tetris. */
    private static final int TETRIS_MULT = 8;
    
    /** Number of points scored for clearing one line. */
    private static final int ROW_SCORE = 100;
    
    /** Spacing between Labels in FlowLayout. */
    private static final int LABEL_WIDTH = 40;
    
    /** Spacing between Labels in FlowLayout. */
    private static final int LABEL_HEIGHT = 20;
    
    /** Score increase for placing a block. */
    private static final int BLOCK_SCORE = 10;
    
    /** SoundPlayer to play WAV audio for music and sound effects. */
    private final SoundPlayer mySound = new SoundPlayer();
    
    /** JLabel used to display the current difficulty level. */
    private JLabel myLevelLabel;
    
    /** JLabel used to display the number of lines cleared. */
    private JLabel myClearedLabel;
    
    /** JLabel used to display the current score in the game. */
    private JLabel myScoreLabel;
    
    /** JLabel used to display number of lines until next level. */
    private JLabel myLinesToLabel;
    
    /** Number of lines cleared. */
    private int myNumCleared;
    
    /** Current level in Tetris, determines difficulty. */
    private int myCurrentLevel;
    
    /** Current score in the Tetris game. */
    private int myCurrentScore;
    
    /** Number of lines needed to advance level. */
    private int myLinesToNextLevel;
        
    /**
     * Constructs a new TetrisInfoPanel which displays the controls and next piece.
     * @param theBoard The Tetris Board that will give the next piece
     */
    public TetrisInfoPanel(final Board theBoard) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(PANEL_SIZE);
        theBoard.addObserver(this);
        
        final TetrisNextPiecePanel nextPanel = new TetrisNextPiecePanel(COMPONENT_SIZE);
        theBoard.addObserver(nextPanel);
        add(Box.createRigidArea(PANEL_SPACE));
        add(nextPanel);
        
        initScorePanel();
        initControlsPanel();
    }
   
    /** Initializes a JPanel that displays the controls. */
    private void initControlsPanel() {
        final JPanel controlsPanel = new JPanel();
        controlsPanel.setBackground(Color.WHITE);
        controlsPanel.setMaximumSize(COMPONENT_SIZE);
        controlsPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        
        controlsPanel.add(new JLabel("<html><b>Game Controls:</b></html>"));
        controlsPanel.add(new JLabel("Move Piece Left: A"));
        controlsPanel.add(new JLabel("Move Piece Right: D"));
        controlsPanel.add(new JLabel("Move Piece Down: S"));
        controlsPanel.add(new JLabel("Rotate Clockwise: Q"));
        controlsPanel.add(new JLabel("Rotate Counter-Clockwise: E"));
        controlsPanel.add(new JLabel("Drop Piece: Space"));
        controlsPanel.add(new JLabel("Pause Game: P"));
        
        add(Box.createRigidArea(PANEL_SPACE));
        add(controlsPanel);

    }
    
    /**
     * Resets the scoreboard for a new game. 
     */
    public void resetScore() {
        myNumCleared = 0;
        myCurrentLevel = 1;
        myCurrentScore = 0;
        myLinesToNextLevel = LINES_NEXT_LEVEL;
        setLabelText();
    }
    
    /** Initializes the scoring panel, keeping track of score, lines cleared,
     * and level.
     */
    private void initScorePanel() {   
        myNumCleared = 0;
        myCurrentLevel = 1;
        myCurrentScore = 0;
        myLinesToNextLevel = LINES_NEXT_LEVEL;
        
        final JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER, LABEL_WIDTH, LABEL_HEIGHT));
        scorePanel.setBackground(Color.WHITE);
        scorePanel.setMaximumSize(COMPONENT_SIZE);
        scorePanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());

        myScoreLabel = new JLabel();
        myClearedLabel = new JLabel();
        myLevelLabel = new JLabel();
        myLinesToLabel = new JLabel();
        
        setLabelText();
        
        final Font labelFont = new Font(myScoreLabel.getFont().getName(), Font.PLAIN, 20);
        myScoreLabel.setFont(labelFont);
        myClearedLabel.setFont(labelFont);
        myLevelLabel.setFont(labelFont);
        
        scorePanel.add(myScoreLabel);
        scorePanel.add(myClearedLabel);
        scorePanel.add(myLevelLabel);
        scorePanel.add(myLinesToLabel);
       
        add(Box.createRigidArea(PANEL_SPACE));
        add(scorePanel);
    }
    
    /** Updates the text for the scoring labels. */
    private void setLabelText() {
        myScoreLabel.setText("Score: " + myCurrentScore);
        myClearedLabel.setText("Lines Cleared: " + myNumCleared);
        myLevelLabel.setText("Difficulty Level: " + myCurrentLevel);
        myLinesToLabel.setText("Lines to next level: " + myLinesToNextLevel);
    }
    
    /** Updates the scores, level, and lines cleared on the
     * scoring panel as the game is played.
     * @param theRowsCleared The array representing the rows that have been cleared.
     */
    private void updateScorePanel(final Integer [] theRowsCleared) {
        myNumCleared += theRowsCleared.length;
        
        final int tempLevel = myCurrentLevel;
        myCurrentLevel = myNumCleared / LINES_NEXT_LEVEL + 1;
        firePropertyChange("level", tempLevel, myCurrentLevel);

        myLinesToNextLevel = (LINES_NEXT_LEVEL * myCurrentLevel) - myNumCleared;
 
        if (theRowsCleared.length < TETRIS_LINES) {
            // 100 points per row cleared.
            myCurrentScore += theRowsCleared.length * ROW_SCORE;
        } else {
            // 800 points for clearing a Tetris + 100 points for additional lines cleared.
            myCurrentScore += ROW_SCORE * TETRIS_MULT 
                            + (theRowsCleared.length - TETRIS_LINES) * ROW_SCORE;
        } 

        setLabelText();
    }
    
    /** Updates the score by 10 for placing a block. */
    private void updateScorePanel() {
        myCurrentScore += BLOCK_SCORE;
        setLabelText();
    }

    /** Receives updates from the board to get lines cleared. 
     *  Updates score when a block is placed on the board.*/
    @Override
    public void update(final Observable theO, final Object theArg) {
        if (theArg instanceof Integer []) {
            updateScorePanel((Integer []) theArg);
            try {
                mySound.play("sounds/lineclear.wav"); 
            } catch (final IllegalArgumentException e) {
                e.printStackTrace();
            }
        } 
        if (theArg instanceof TetrisPiece) {
            updateScorePanel();
        }
        
    }
}
