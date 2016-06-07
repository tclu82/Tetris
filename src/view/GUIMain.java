/*
 * TCSS 305 Assignment 6 - Tetris
 */

package view;

import java.awt.EventQueue;

/**
 * Starts The Tetris.
 *
 * @author Tzu-Chien Lu
 * @version Dec 11 2015
 */
public final class GUIMain {

    /**
     * Private constructor to inhibit instantiation.
     */
    private GUIMain() {
        throw new IllegalStateException();
    }
    
    /**
     * Start point for the program.
     *
     * @param theArgs command line arguments - ignored
     */
    public static void main(final String... theArgs) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().start();
            }
        });
    }
}
