
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Point;
import model.TetrisPiece;

/**
 * Defines a JPanel that is used to display the next piece on the Tetris Board.
 * 
 * @author Michael Lambion
 * @version 2016.05.24
 *
 */
public class TetrisNextPiecePanel extends JPanel implements Observer {

    /** Generated serial version UID. */
    private static final long serialVersionUID = -1976124895218360118L;

    /** The size of the blocks used to display the next Tetris piece. */
    private static final int BLOCK_SIZE = 30;
    
    /** List of blocks to be drawn in the Next Piece pane. */
    private final List<Rectangle2D> myBlocks;

    /** The color of the blocks for the next piece. */
    private Color myColor;
    

    /** Initializes and constructs the Next Piece pane to show
         * the Next Piece to appear on the board.
         * @param theSize The Dimensions for the size of the JPanel.
         */
    public TetrisNextPiecePanel(final Dimension theSize) {
        super();
        myBlocks = new ArrayList<Rectangle2D>();

        setBackground(Color.WHITE);
        setMaximumSize(theSize);
        setBorder(BorderFactory.createLoweredSoftBevelBorder());
    }

    /**
     * Sets the next piece's shape and orientation.
     * @param thePiece The TetrisPiece to be shown in the 'Next Shape' panel.
     */
    private void setNextShape(final TetrisPiece thePiece) {

        myBlocks.clear();
        final Point[] piecePoints = thePiece.getPoints();
        for (final Point p : piecePoints) {
            final int xCoord;
            if ("I".equals(thePiece.toString())) {
                xCoord = p.x() * BLOCK_SIZE - BLOCK_SIZE
                         + BLOCK_SIZE * thePiece.getWidth() / 2;
            } else {
                xCoord = p.x() * BLOCK_SIZE + BLOCK_SIZE * thePiece.getWidth() / 2;
            }
            final int yCoord = p.y() * BLOCK_SIZE + BLOCK_SIZE * thePiece.getHeight() / 2;

            myBlocks.add(new Rectangle2D.Double(xCoord, yCoord, BLOCK_SIZE, BLOCK_SIZE));

        }
        setColor(thePiece.getBlock().getChar());
    }
    
    /**
     * Sets the color for the given block type.
     * @param theBlockType Character that represents a type of Tetris piece. 
     */
    private void setColor(final char theBlockType) {
        switch (theBlockType) {
            case 'I':
                myColor = Color.CYAN;
                break;
            case 'J':
                myColor = Color.BLUE;
                break;
            case 'L':
                myColor = Color.ORANGE;
                break;
            case '0':
                myColor = Color.YELLOW;
                break;
            case 'S':
                myColor = Color.GREEN;
                break;
            case 'T':
                myColor = Color.PINK;
                break;
            case 'Z':
                myColor = Color.RED;
                break;
            default:
                System.err.println("Did you add a new shape?");
                break;
        }
    }

    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.translate(0, getSize().getHeight());
        g2d.scale(1, -1);

        for (final Rectangle2D r : myBlocks) {
            g2d.setPaint(myColor);
            g2d.fill(r);
            g2d.setPaint(Color.BLACK);
            g2d.draw(r);
        }
    }
    
    /** 
     * Gets the next TetrisPiece to be displayed in this panel.
     */
    @Override
    public void update(final Observable theO, final Object theArg) {
        if (theArg instanceof TetrisPiece) {
            setNextShape((TetrisPiece) theArg);
            repaint();
        }
    } 
}
