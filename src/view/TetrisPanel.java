package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import model.Board;

/**
 * This class draws a graphical representation of a Board object
 * that can be used to play Tetris.
 * @author Michael Lambion
 * @version 2016.05.23
 */
public class TetrisPanel extends JPanel implements Observer {
    
    /** Generated serial version UID. */
    private static final long serialVersionUID = -2120080126459055695L;
    
    /** Lines to skip in the Board's toString. */
    private static final int NUM_LINES = 5;
    
    /** The Board represented by this TetrisPanel. */
    private final Board myTetrisBoard;

    /** The size of the Tetris piece blocks to be drawn. */
    private int myBlockSize;
    
    /** Map of blocks to be drawn on the board. */
    private final Map<Rectangle2D, Color> myTetrisBlocks;
    
    /** Boolean to check if the game is paused. */
    private boolean myIsPaused;
    
    /**
     * Constructs a new graphical representation of a Board object.
     * @param theBoard The Board to be drawn.
     * @param theBlockSize The size of the Tetris blocks to be drawn.
     */
    public TetrisPanel(final Board theBoard, final int theBlockSize) {
        super();
        myTetrisBoard = theBoard;
        resizeBlocks(theBlockSize);
        setBackground(Color.WHITE);
        setOpaque(true);
        myTetrisBlocks = new HashMap<Rectangle2D, Color>();
        theBoard.addObserver(this);

    }
    
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;      
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (final Rectangle2D key : myTetrisBlocks.keySet()) {
            g2d.setPaint(myTetrisBlocks.get(key));
            g2d.fill(key);
            g2d.setPaint(Color.BLACK);
            g2d.draw(key); 
        }
        if (myIsPaused) {
            final Color pauseFill = new Color(0f, 0f, 0f, .75f);
            g2d.setPaint(pauseFill);
            g2d.fill(new Rectangle2D.Double(0, 0, 
                                            getSize().getWidth(), getSize().getHeight()));
        }
    } 

    @Override
    public void update(final Observable theO, final Object theArg) {
        if (theArg instanceof String) {
            try {
                findBlocks(((Board) theO).toString());
            } catch (final IOException e) {
                e.printStackTrace();
            }          
        } else if (theArg instanceof Boolean) {
            myIsPaused = (Boolean) theArg;
            repaint();
        }
    }
    
    /**
     * Resizes the board and Tetris blocks to the given size.
     * @param theBlockSize The size to resize the Tetris blocks to.
     */
    public void resizeBlocks(final int theBlockSize) {
        myBlockSize = theBlockSize;
        setPreferredSize(new Dimension(myBlockSize * myTetrisBoard.getWidth() + 1
                                       , myBlockSize * myTetrisBoard.getHeight()));
        repaint();
    }
    
    /**
     * Builds a list of TetrisBlock objects that represent the current state
     * of the Tetris Board based on the Board's toString.
     * @param theBoard The Board to draw tetris blocks for.
     * @throws IOException 
     */
    private void findBlocks(final String theBoard) throws IOException {
        myTetrisBlocks.clear();
        final BufferedReader br = new BufferedReader(new StringReader(theBoard));
        for (int i = 0; i < NUM_LINES; i++) {
            br.readLine();
        }
        for (int i = 0; i < myTetrisBoard.getHeight(); i++) {
            String line = "";
            try {
                line = br.readLine();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            for (int j = 1; j <= myTetrisBoard.getWidth(); j++) {
                if (line.charAt(j) != ' ') {
                    drawBlocks(line.charAt(j), j - 1, i);
                }
            }
        }
        repaint();
    }
    
    /**
     * Creates a new Rectangle2D to represent the state of a Tetris block.
     * @param theBlock The char representation for the type of Tetris block.
     * @param theX The relative x-coordinate of the Tetris block.
     * @param theY The relative y-coordinate of the Tetris block.
     */
    private void drawBlocks(final char theBlock, final int theX, final int theY) {
        final Rectangle2D block = new Rectangle2D.Double(theX * myBlockSize, theY * myBlockSize
                                         , myBlockSize, myBlockSize);
        Color blockColor = null;
        switch (theBlock) {
            case 'I': blockColor = Color.CYAN;
                      break;
            case 'J': blockColor = Color.BLUE;
                      break;
            case 'L': blockColor = Color.ORANGE;
                      break;
            case '0': blockColor = Color.YELLOW;
                      break;
            case 'S': blockColor = Color.GREEN;
                      break;
            case 'T': blockColor = Color.PINK;
                      break;
            case 'Z': blockColor = Color.RED;
                      break;
            default:  System.err.println("Did you add a new shape?");
                      break;
        }
        myTetrisBlocks.put(block, blockColor);
    }
    
}
