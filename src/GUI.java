package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import DDO_Solver.MultipleBoard;
import DDO_Solver.Solver;


/**
 * GUI
 */
public class GUI {
    private JFrame frame;
    private JLabel moveLabel;
    private JLabel winningLabel;
    private JLabel noSolutionLabel;
    private JButton[][] tileButtons;
    private JButton instructionButton;
    private JButton newGameButton;
    private JButton editButton;
    private JButton playButton;
    private JButton solveButton;    
    private JComboBox<String> sizeChoices;


    public GUI() {
        openFrame();
        drawMoveLabel();
        drawWinningLabel();
        drawNoSolutionLabel();
        drawTileButtons();
        drawInstructionButton();
        drawNewGameButton();
        drawEditButton();
        drawPlayButton();
        drawSolveButton();
        drawChoicesComboBox();
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
        moveLabel.setBounds(50, 175, 100, 50);
        frame.add(moveLabel);
    }


    // Add label for displaying the winning text
    public void drawWinningLabel() {
        winningLabel = new JLabel("YOU WON!!!");
        winningLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        winningLabel.setSize(150, 100);
        winningLabel.setLocation((frame.getWidth() - winningLabel.getWidth()) / 2, 100);
        winningLabel.setVisible(false);
        frame.add(winningLabel);
    }


    // Add "No Solution" label if there is no solution for the board
    public void drawNoSolutionLabel() {
        noSolutionLabel = new JLabel("NO SOLUTION!");
        noSolutionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));
        noSolutionLabel.setSize(200, 100);
        noSolutionLabel.setLocation((frame.getWidth() - winningLabel.getWidth()) / 2, 100);
        noSolutionLabel.setVisible(false);
        frame.add(noSolutionLabel);
    }


    // Add the main (game) buttons
    public void drawTileButtons() {
        tileButtons = new JButton[Board.getSize()][Board.getSize()];
        int counter = 0;
        int squareSize = (frame.getWidth() - 100) / Board.getSize();
        for (int row = 0; row < Board.getSize(); row++) {
            for (int col = 0; col < Board.getSize(); col++) {
                tileButtons[row][col] = new JButton(String.valueOf(counter++));
                tileButtons[row][col].setFont(new Font(Font.SANS_SERIF, Font.BOLD, (squareSize + 5) / 5));
                tileButtons[row][col].setBounds(50 + squareSize * col, 225 + squareSize * row,
                                                squareSize, squareSize);
                setSquareButtonBackgroundCol(row, col);
                tileButtons[row][col].addActionListener(new PlayButtonActionListener(row, col));
                frame.add(tileButtons[row][col]);
            }
        }
    }


    // Add Instruction (question mark: ?) label for instruction
    public void drawInstructionButton() {
        ImageIcon imageIcon = new ImageIcon("Images/cropped_blue_question_mark.png");
        // Get the image from the imageIcon
        Image image = imageIcon.getImage();
        // Resize the image
        Image newImage = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newImage);

        // Create the label to add the imageIcon
        instructionButton = new JButton(imageIcon);
        instructionButton.setSize(imageIcon.getIconHeight(), imageIcon.getIconWidth());
        instructionButton.setLocation(frame.getWidth() - 40, 5);
        // Make the button round and does not have rectangular shape
        instructionButton.setContentAreaFilled(false);
        // Hide the black rectangle when clicking the button
        instructionButton.setBorderPainted(false);

        // Add ActionListener to the instructionButton
        instructionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add a new frame for instruction
                JFrame instructionFrame = new JFrame("Instruction");
                instructionFrame.setSize(500, 700);

                // Add a panel
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

                // Add a welcome to the instructionFrame
                JLabel welcomeInstructLabel = new JLabel("Welcome to the DDO Vale Puzzle");
                welcomeInstructLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
                welcomeInstructLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(welcomeInstructLabel);

                // Add "Instruction" text to the instructionFrame
                JLabel instructionLabel = new JLabel("Instruction");
                instructionLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
                instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(instructionLabel);

                // Add the instruction
                // String instructionText = "The goal of the puzzle is to turn all the tiles' lights" +
                //                          "of the board on. The light of the tile is on if the tile" +
                //                          "background color is cyan and the light of the tile is off" +
                //                          "if its background color is light gray";
                String instructionText = "<html>" +
                                         "&nbsp The goal of the puzzle is to turn all the tiles\' lights of the board on." +
                                         "<br>" +
                                         "&nbsp The light of the tile is on if the tile background color is cyan " +
                                            "and the light of the tile is off if its background color is light gray." +
                                         "<br>" +
                                         "&nbsp When clicking a tile, you will flip the light state (color) of that tile " +
                                            "and the light state of the tiles that are horizontally and " +
                                            "vertically adjacent to the current tile." +
                                         "<br>" +
                                         "&nbsp Flipping the light state of a tile means that if the current light state " +
                                            "of a tile is on, flipping the light state will turn it off " +
                                            "and vice versa." +
                                         "<br>" +
                                         "&nbsp Click on the \"New Game\" button to create a new board." +
                                         "<br>" +
                                         "&nbsp Click on the \"Edit\" button to edit the light state of a tile. " +
                                            "\"Edit\" button will only flip the light state of the tile " +
                                            "being clicked and does not flip the adjacent tiles' light states." +
                                         "<br>" +
                                         "&nbsp Click on the \"Play\" button to continue playing after editing." +
                                         "<br>" +
                                         "&nbsp Click on the \"Solve\" button to get the moves needed to solve the board. " +
                                            "The number on the tile that needs clicking to solve the board " +
                                            "will be colored red." +
                                         "</html>";
                JLabel instruction = new JLabel(instructionText);
                instruction.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
                instruction.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(instruction);


                // Close the instructionFrame
                instructionFrame.add(panel);
                instructionFrame.setResizable(false);
                instructionFrame.setBackground(Color.white);
                instructionFrame.setVisible(true);
                instructionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });

        frame.add(instructionButton);
    }


    // Add "New Game" button
    public void drawNewGameButton() {
        newGameButton = new JButton("New Game");
        newGameButton.setBounds(30, 30, 100, 40);
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
                        // Redraw the main (game) buttons background color                   
                        setSquareButtonBackgroundCol(row, col);

                        // Reset the color of the text to black
                        // (if the text color is red when the "Solve" button is clicked)
                        tileButtons[row][col].setForeground(Color.black);

                        // Hide the noSolutionLabel
                        noSolutionLabel.setVisible(false);

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
        editButton.setBounds(140, 30, 100, 40);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int row = 0; row < Board.getSize(); row++) {
                    for (int col = 0; col < Board.getSize(); col++) {
                        rmAllALAndAddAL(row, col, "Edit");

                        // Reset the color of the text to black
                        // (if the text color is red when the "Solve" button is clicked)
                        tileButtons[row][col].setForeground(Color.black);

                        // Hide the noSolutionLabel
                        noSolutionLabel.setVisible(false);
                    }
                }
            }
        });
        frame.add(editButton);
    }


    // Add "Play" button
    public void drawPlayButton() {
        playButton = new JButton("Play");
        playButton.setBounds(250, 30, 100, 40);
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

    
    // Add "Solve" button
    public void drawSolveButton() {
        solveButton = new JButton("Solve");
        solveButton.setBounds(360, 30, 100, 40);
        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MultipleBoard solvedBoard = Solver.solvePuzzle();

                // If there is NO solution to the board
                if (solvedBoard == null) {
                    // Print "NO SOLUTION" out
                    noSolutionLabel.setVisible(true);
                } else {
                    // If there is a solution to the board
                    for (int row = 0; row < solvedBoard.getLengthSize(); row++) {
                        for (int col = 0; col < solvedBoard.getWidthSize(); col++) {
                            if (solvedBoard.getBoard(row, col).isLightOn()) {
                                // Set the text color of the button to red
                                tileButtons[row][col].setForeground(Color.red);
                            } else {
                                tileButtons[row][col].setForeground(Color.black);
                            }
                        }
                    }
                }
            }
        });
        frame.add(solveButton);
    }


    // Add choices drop down
    public void drawChoicesComboBox() {
        String[] sizeChoicesString = { "3 x 3", "4 x 4", "5 x 5", "6 x 6", "7 x 7", "8 x 8", "9 x 9" };
        sizeChoices = new JComboBox<String>(sizeChoicesString);
        sizeChoices.setSize(60, 20);
        sizeChoices.setLocation((frame.getWidth() - sizeChoices.getWidth()) / 2, 85);
        sizeChoices.setSelectedIndex(0);
        sizeChoices.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> cb = (JComboBox<String>) e.getSource();             
                String sizeString = (String) cb.getSelectedItem();
                int size = Character.getNumericValue(sizeString.charAt(0));
                
                // Remove current tile buttons and update the frame GUI
                for (JButton[] tileButtonsArr : tileButtons) {
                    for (JButton tileButton : tileButtonsArr) {
                        frame.remove(tileButton);
                    }
                }
                frame.revalidate();
                frame.repaint();

                // Generate a new board with new size
                Board.init(size);

                // Reset and display the number of moves
                Board.setMoves(0);
                moveLabel.setText("Moves: " + String.valueOf(Board.getMoves()));

                // Re-add tile buttons with new board size
                drawTileButtons();

                // Disable winning text button
                winningLabel.setVisible(false);
            }
        });
        frame.add(sizeChoices);
    }


    // Close the frame
    public void closeFrame() {
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setBackground(Color.white);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    // ************* PRIVATE METHODS AND CLASSES ****************
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

            // Flip only the light of the current button
            Board.flipLight(this.currentRow, this.currentCol);

            // Re-display the color of that button
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