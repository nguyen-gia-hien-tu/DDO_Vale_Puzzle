package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * GUI
 */
public class GUI {
    private JFrame frame;
    private JButton[][] tileButtons;
    private JButton newGameButton;
    private JButton editButton;
    private JButton playButton;
    private JLabel winningLabel;
    private JLabel moveLabel;


    public GUI() {
        openFrame();
        drawMoveLabel();
        drawWinningLabel();
        drawTileButtons();
        drawNewGameButton();
        drawEditButton();
        drawPlayButton();
        closeFrame();
    }


    public void openFrame() {
        // Prepare the frame
        int frameWidth = 500;
        int frameHeight = 700;
        frame = new JFrame("DDO Vale Puzzle");        
        frame.setSize(frameWidth, frameHeight);
    }


    // Add label for displaying the number of moves
    public void drawMoveLabel() {
        moveLabel = new JLabel("Moves: " + String.valueOf(Board.getMoves()));
        moveLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        moveLabel.setBounds(40, 150, 100, 50);
        frame.add(moveLabel);
    }


    // Add label for displaying the winning text
    public void drawWinningLabel() {
        winningLabel = new JLabel("YOU WON!!!");
        winningLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        winningLabel.setSize(150, 100);
        winningLabel.setLocation((frame.getWidth() - winningLabel.getWidth()) / 2, 65);
        winningLabel.setVisible(false);
        frame.add(winningLabel);
    }


    // Add the main (game) buttons
    public void drawTileButtons() {
        tileButtons = new JButton[Board.getSize()][Board.getSize()];
        int counter = 0;
        int squareSize = (frame.getWidth() - 100) / Board.getSize();
        for (int row = 0; row < Board.getSize(); row++) {
            for (int col = 0; col < Board.getSize(); col++) {
                tileButtons[row][col] = new JButton(String.valueOf(counter++));
                tileButtons[row][col].setBounds(40 + squareSize * col, 200 + squareSize * row,
                                                squareSize, squareSize);
                setSquareButtonBackgroundCol(row, col);
                tileButtons[row][col].addActionListener(new PlayButtonActionListener(row, col));
                frame.add(tileButtons[row][col]);
            }
        }
    }


    // Add "New Game" button
    public void drawNewGameButton() {
        newGameButton = new JButton("New Game");
        newGameButton.setBounds(90, 20, 100, 40);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Generate a new board
                Board.init(Board.getSize());
                // Reset and display the number of moves
                Board.setMoves(0);
                moveLabel.setText("Moves: " + String.valueOf(Board.getMoves()));

                // Reset the main (game) buttons background color
                for (int row = 0; row < Board.getSize(); row++) {
                    for (int col = 0; col < Board.getSize(); col++) {                        
                        setSquareButtonBackgroundCol(row, col);

                        // Enable main (square) buttons
                        tileButtons[row][col].setEnabled(true);

                        // Remove all Action Listeners of the button and
                        // Add Action Listener to flip the lights of the
                        // current and adjacent buttons
                        rmAllALAndAddAL(row, col, "Play");                       
                    }
                }

                // Hide winning text
                winningLabel.setVisible(false);
            }
        });
        frame.add(newGameButton);
    }


    // Add "Edit" button
    public void drawEditButton() {
        editButton = new JButton("Edit");
        editButton.setBounds(200, 20, 100, 40);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int row = 0; row < Board.getSize(); row++) {
                    for (int col = 0; col < Board.getSize(); col++) {
                        rmAllALAndAddAL(row, col, "Edit");
                    }
                }
            }
        });
        frame.add(editButton);
    }


    // Add "Play" button
    public void drawPlayButton() {
        playButton = new JButton("Play");
        playButton.setBounds(310, 20, 100, 40);
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int row = 0; row < Board.getSize(); row++) {
                    for (int col = 0; col < Board.getSize(); col++) {
                        rmAllALAndAddAL(row, col, "Play");
                    }
                }
            }
        });
        frame.add(playButton);
    }


    // Close the frame
    public void closeFrame() {
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setBackground(Color.white);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    // ******************** PRIVATE METHODS *********************
    // Private method to set the color of the main (game) buttons
    private void setSquareButtonBackgroundCol(int row, int col) {
        if (Board.getBoard(row, col).isLightOn()) {
            tileButtons[row][col].setBackground(Color.cyan);
        } else {
            tileButtons[row][col].setBackground(Color.lightGray);
        }
    }


    // Private method to remove all ActionListeners of a current button
    // from buttons and add a specific ActionListener to it
    private void rmAllALAndAddAL(int row, int col, String alType) {
        // Remove all Action Listeners of the button
        for (ActionListener al : tileButtons[row][col].getActionListeners()) {
            tileButtons[row][col].removeActionListener(al);
        }
        // Add Action Listener to only flip the current button
        // and not flip adjacent buttons
        if (alType.equals("Play"))
            tileButtons[row][col].addActionListener(new PlayButtonActionListener(row, col));
        else
            tileButtons[row][col].addActionListener(new EditButtonActionListener(row, col));
    }


    // Private class to handle "Edit" button action event
    private class EditButtonActionListener implements ActionListener {
        private int currentRow, currentCol;

        EditButtonActionListener(int r, int c) {
            this.currentRow = r;
            this.currentCol = c;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Reset the number of moves
            Board.setMoves(0);
            // Update displaying the number of moves
            moveLabel.setText("Moves: " + String.valueOf(Board.getMoves()));

            // Flip the light of the current button
            Board.flipLight(this.currentRow, this.currentCol);

            // Display the color of that button again
            setSquareButtonBackgroundCol(this.currentRow, this.currentCol);
        }
    }


    // Private class to handle "Play" button action event
    private class PlayButtonActionListener implements ActionListener {
        private int currentRow, currentCol;

        PlayButtonActionListener(int r, int c) {
            this.currentRow = r;
            this.currentCol = c;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Flip the adjacent tiles light to on or off
            Board.tileIsClicked(this.currentRow, this.currentCol);
            // Increase the number of moves
            Board.incMoves();
            // Update displaying the number of moves
            moveLabel.setText("Moves: " + String.valueOf(Board.getMoves()));
    
            // Redraw the tile buttons
            for (int row = 0; row < Board.getSize(); row++) {
                for (int col = 0; col < Board.getSize(); col++) {
                    setSquareButtonBackgroundCol(row, col);
                }
            }
            
            // If the game is won
            if (Board.allTilesOn()) {
                // Display winning text
                winningLabel.setVisible(true);

                // Disable main (game) buttons
                for (int row = 0; row < Board.getSize(); row++) {
                    for (int col = 0; col < Board.getSize(); col++) {
                        tileButtons[row][col].setEnabled(false);
                    }
                }
            }
        }
    }   
}