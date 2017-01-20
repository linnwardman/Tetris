package view;

import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Utility class used to set the look and 
 * feel for GUI being started.
 * @author Michael Lambion
 * @version 2016.05.23
 */
public final class TetrisMain {
    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private TetrisMain() {
        
    }
    
    /**
     * Sets look and feel and starts the Tetris program.
     * @param theArgs Unused.
     */
    public static void main(final String[] theArgs) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (final UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGUI().start();
            }
        });
    }

}
