package view;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;


/**
 * Constructs a menu bar with options for the Tetris game.
 * @author Michael Lambion
 * @version 2016.05.28
 */
public class TetrisMenuBar extends JMenuBar implements Observer {
    /** Generated serial version UID. */
    private static final long serialVersionUID = 7944054884923809712L;
    
    /** Constant used to define width/height of a Tetris board. */
    private static final int SMALL_BOARD = 10;
    
    /** Constant used to define width/height of a Tetris board. */
    private static final int MED_BOARD = 20;
    
    /** Constant used to define width/height of a Tetris board. */
    private static final int LARGE_BOARD = 30;
    
    /** Defines block size for resizing to small. */
    private static final int SMALL = 30;
    
    /** Defines block size for resizing to medium. */
    private static final int MED = 40;
   
    /** Defines block size for resizing to large. */
    private static final int LARGE = 50;
    
    /** Timer delay reduction (milliseconds) for easy difficulty. */
    private static final int SLOW = 50;
    
    /** Timer delay reduction (milliseconds) for normal difficulty. */
    private static final int NORMAL = 100;
    
    /** Timer delay reduction (milliseconds) for hard difficulty. */
    private static final int HARD = 200;
    
    /** Defines string to display in resizing options. */
    private static final String TIMES = " X ";
    
    /** Menu option to start a new game. */
    private final JMenuItem myNewGame;
    
    /** Menu options to end the current game. */
    private final JMenuItem myEndGame;
    

    /**
     * Sets up the Tetris GUI's menu bar.
     * @param theGUI The GUI for the options to act upon.
     * @param theFrame The JFrame to close on Quit.
     */
    public TetrisMenuBar(final TetrisGUI theGUI, final JFrame theFrame) {
        super();
        myNewGame = new JMenuItem("New Game");
        myEndGame = new JMenuItem("End Game");
        theGUI.addObserver(this);
        initGameMenu(theGUI, theFrame);
        initOptionMenu(theGUI);
        initAboutMenu();
    }
    
    /**
     * Sets up the Game menu for the Tetris menu bar.
     * @param theGui The GUI which contains the timer to run the Tetris game.
     * @param theFrame The JFrame to close on Quit.
     */
    private void initGameMenu(final TetrisGUI theGui, final JFrame theFrame) {
        final JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        add(gameMenu);
        
        myNewGame.setMnemonic(KeyEvent.VK_N);
        myNewGame.addActionListener((theEvent) -> {
            theGui.newGame();
        });
        gameMenu.add(myNewGame);
        
        myEndGame.setEnabled(false);
        myEndGame.setMnemonic(KeyEvent.VK_E);
        myEndGame.addActionListener((theEvent) -> {
            theGui.endGame();
        });
        gameMenu.add(myEndGame);
        
        final JSeparator separator = new JSeparator();
        gameMenu.add(separator);
        
        final JMenuItem quit = new JMenuItem("Quit");
        quit.setMnemonic(KeyEvent.VK_Q);
        quit.addActionListener((theEvent) -> {
            theFrame.dispatchEvent(new WindowEvent(theFrame, WindowEvent.WINDOW_CLOSING));
        });
        gameMenu.add(quit);
    }
    
    /**
     * Sets up the options menu for the Tetris menu bar.
     * @param theGUI The GUI for the options to act upon.
     */
    private void initOptionMenu(final TetrisGUI theGUI) {
        final JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic(KeyEvent.VK_O);
        add(optionsMenu);
        
        optionsMenu.add(initResizeMenu(theGUI));
        optionsMenu.add(initResizeWindowMenu(theGUI));
        optionsMenu.add(initDifficultyMenu(theGUI));
    }
    
    /**
     * Creates Menu to resize blocks.
     * @param theGUI The GUI to resize.
     * @return JMenu containing the block resize options.
     */
    private JMenu initResizeMenu(final TetrisGUI theGUI) {
        final JMenu resizeWindow = new JMenu("Resize Blocks");
        resizeWindow.setMnemonic(KeyEvent.VK_R);
        
        final ButtonGroup sizeButtons = new ButtonGroup();
        final JRadioButton small = new JRadioButton(SMALL + TIMES + SMALL);
        small.addActionListener((theEvent) -> {
            theGUI.resizePanel(SMALL);
        });
        sizeButtons.add(small);
        resizeWindow.add(small);

        final JRadioButton medium = new JRadioButton(MED + TIMES + MED);
        medium.addActionListener((theEvent) -> {
            theGUI.resizePanel(MED);
        });
        sizeButtons.add(medium);
        resizeWindow.add(medium);
        
        final JRadioButton large = new JRadioButton(LARGE + TIMES + LARGE);
        large.addActionListener((theEvent) -> {
            theGUI.resizePanel(LARGE);
        });
        sizeButtons.add(large);
        resizeWindow.add(large);
        
        small.doClick();
        return resizeWindow;
         
    }
    
    /**
     * Creates the menu to resize the board to play Tetris.
     * @param theGUI The GUI containing a representation of the board.
     * @return JMenu containing board resize options.
     */
    private JMenu initResizeWindowMenu(final TetrisGUI theGUI) {
        final JMenu boardSize = new JMenu("Resize Board");
        boardSize.setMnemonic(KeyEvent.VK_R);
        
        boardSize.add(new JLabel("Resize ends current game."));
        boardSize.addSeparator();
        
        final ButtonGroup boardSizeButtons = new ButtonGroup();
        final JRadioButton defaultBoard = new JRadioButton(SMALL_BOARD + TIMES + MED_BOARD);
        defaultBoard.addActionListener((theEvent) -> {
            theGUI.resizeBoard(SMALL_BOARD, MED_BOARD);
        });
        boardSizeButtons.add(defaultBoard);
        boardSize.add(defaultBoard);
        
        final JRadioButton largeBoard = new JRadioButton(MED_BOARD + TIMES + LARGE_BOARD);
        largeBoard.addActionListener((theEvent) -> {
            theGUI.resizeBoard(MED_BOARD, LARGE_BOARD);
        });
        boardSizeButtons.add(largeBoard);
        boardSize.add(largeBoard);
        
        return boardSize;
    }
    
    /**
     * Creates the Difficulty menu to adjust the time decrease on level up. 
     * @param theGUI The GUI containing the Tetris board.
     * @return The JMenu with options to change timer decrease.
     */
    private JMenu initDifficultyMenu(final TetrisGUI theGUI) {
        final JMenu difficulty = new JMenu("Difficulty Level");
        difficulty.setMnemonic(KeyEvent.VK_D);
        
        final ButtonGroup diffButtons = new ButtonGroup();
        final JRadioButton easyButton =
                        new JRadioButton("Easy - 0.05 second reduction/level.");
        easyButton.addActionListener((theEvent) -> {
            theGUI.setDifficulty(SLOW);
        });
        diffButtons.add(easyButton);
        difficulty.add(easyButton);
        
        final JRadioButton normalButton = 
                        new JRadioButton("Normal - 0.1 second reduction/level.");
        normalButton.addActionListener((theEvent) -> {
            theGUI.setDifficulty(NORMAL);
        });
        diffButtons.add(normalButton);
        difficulty.add(normalButton);
        normalButton.setSelected(true);
        
        final JRadioButton hardButton = 
                        new JRadioButton("Hard - 0.2 second reduction/level.");
        hardButton.addActionListener((theEvent) -> {
            theGUI.setDifficulty(HARD);
        });
        diffButtons.add(hardButton);
        difficulty.add(hardButton);
        
        return difficulty;
    }
    
    /**
     * Sets up the About menu to display information about the game and program.
     */
    private void initAboutMenu() {
        final JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_A);
        
        final JMenuItem scoring = new JMenuItem("Scoring");
        scoring.setMnemonic(KeyEvent.VK_S);
        scoring.addActionListener((theEvent) -> {    
            JOptionPane.showMessageDialog(null
                            , "Scoring Rules: \n- 10 points per block placed.\n"
                                            + "- 100 Points per line cleared.\n"
                                            + "- 800 Points per Tetris "
                                            + "(4 lines cleared at once).\n"
                                            + "- Delay between blocks moving is 1 second\n"
                                            + "  and is reduced by 0.1 seconds every level."
                            , "Scoring Rules", JOptionPane.INFORMATION_MESSAGE);
        }); 
        
        final JMenuItem about = new JMenuItem("About...");
        about.setMnemonic(KeyEvent.VK_A);
        about.addActionListener((theEvent) -> {    
            JOptionPane.showMessageDialog(null
                            , "Tetris - TCSS 305 - Michael Lambion"
                                + "\n\nTetris theme from Nintendo's Tetris (Game Boy), 1989."
                                + "\nTetris end-game sound from "
                                + "Nintendo's Tetris (Game Boy), 1989."
                                + "\n\nClear line and movement blip "
                                + "sounds generated using Bfxr (bfxr.net)"
                                , "About and Credits", JOptionPane.INFORMATION_MESSAGE);
        }); 
        helpMenu.add(scoring);
        helpMenu.add(about);
        add(helpMenu);
    }
    
    /** Toggles the enabled state of the New Game and End Game menu items. */
    private void toggleGameButtons() {
        myNewGame.setEnabled(!(myNewGame.isEnabled()));
        myEndGame.setEnabled(!(myEndGame.isEnabled()));
    }

    /**
     * Toggles the game button when a game is started or ended.
     * @param theO The Observable object, the Board.
     * @param theArg Boolean representing if the game has ended.
     */
    @Override
    public void update(final Observable theO, final Object theArg) {
        if (theArg instanceof Boolean) {
            toggleGameButtons();
        }  
    }
}
