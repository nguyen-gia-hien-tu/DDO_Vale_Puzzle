package DDO_Solver;

import src.Board;

public class Solver {
    // Solve the puzzle
    // The idea:
    //      L + sum_{i, j} (x_{i j} A_{i j}) = 1
    // where L       is the given board,
    //       A_{i j} is the board where only the tile
    //               at row i, column j is clicked
    //       x_{i j} is the number of times the board
    //               A_{i j} needs to appear. In this case,
    //               x_{i j} is either 0 or 1 since we only
    //               need to click a tile zero or one time.
    //               Clicking the tile 2 times is the same
    //               as not clicking the tile
    //       1       is the matrix where all entries are 1 (light on)  
    // x_{i j} is the ones we need to find
    // Solve the equation:
    //      sum_{i, j} (x_{i j} A_{i j}) = 1 - L
    // Hence, to solve this equation, we need to solve the augmented matrix:
    //      A x = 1 - L
    // where each entry at row r, column c of the matrices A_{i j}
    // form the row (A_{i j}.length * r + c) in the big matrix A
    // where A_{i j}.length is the number of row in a matrix A_{i j}
    // Note: the number of rows in all matrices A_{i j} is the same
    //       as well as the number of columns
    // 1 - L is in the one column format
    // Note: 1 - L is the matrix where all entries in the matrix L
    //       is flipped (0 -> 1, 1 -> 0)
    public static MultipleBoard solvePuzzle() {
        // STEP 1: Create the comboBoard
        // Create an array of boards where only one tile is clicked
        MultipleBoard[] oneTileClickedBoards = new MultipleBoard[Board.getSize() * Board.getSize()];
        for (int i = 0; i < oneTileClickedBoards.length; i++) {
            oneTileClickedBoards[i] = new MultipleBoard(Board.getSize(), Board.getSize());
            oneTileClickedBoards[i].flipCurrAndAdjLights(i / Board.getSize(), i % Board.getSize());
        }

        // To create comboBoard, the light state at position [row][col]
        // (where 0 <= row < Board.getSize(), 0 <= col < Board.getSize())
        // of each board in the oneTileClickedBoards
        // will form the row (boardSize * row + col) of the comboBoard
        MultipleBoard comboBoard = new MultipleBoard(Board.getSize() * Board.getSize(), 
                                                     Board.getSize() * Board.getSize());       
        for (int row = 0; row < Board.getSize(); row++) {
            for (int col = 0; col < Board.getSize(); col++) {
                for (int i = 0; i < oneTileClickedBoards.length; i++) {
                    comboBoard.setBoard(Board.getSize() * row + col, i,
                                        oneTileClickedBoards[i].getBoard(row, col).getLightState());
                }
            }
        }

        // STEP 2: Convert the given board into the one column reversed light board
        // Convert the Board into MultipleBoard
        MultipleBoard givenBoard = new MultipleBoard(Board.getSize(), Board.getSize());
        for (int row = 0; row < Board.getSize(); row++) {
            for (int col = 0; col < Board.getSize(); col++) {
                givenBoard.setBoard(row, col, Board.getBoard(row, col).getLightState());
            }
        }

        // Reverse the light of all entries of the givenBoard (i.e. 1 - L)
        MultipleBoard revLightBoard = reverseLightBoard(givenBoard);
        // One Columnize the revLightBoard
        MultipleBoard oneColRevLightBoard = oneColumnize(revLightBoard);

        // STEP 3: Perform reduced row echelon (in modulus 2) on comboBoard
        //         and apply the same operation on each step to the oneColRevLightBoard.
        //         The result of the onColRevLightBoard is the expected "solution" board
        //         before checking for no solution (inconsistent in the matrices)
        // Perform binaryRREFTwoMatrices on the comboBoard and the oneColRevLightBoard
        MultipleBoard[] resultBoards = binaryRREFTwoMatrices(comboBoard, oneColRevLightBoard);
        MultipleBoard comboBoardRREF = resultBoards[0];
        MultipleBoard solutionBoard = resultBoards[1];

        /* Check if the givenBoard is solvable, that is the augmented matrix
         * formed from the comboBoardRREF and solutionBoard is consistent.
         * If the givenBoard is solvable, then return the solutionBoard
         * in an m x n format (not the one column format).
         * If the givenBoard is NOT solvable then return null.
         */
        if (isSolvable(comboBoardRREF, solutionBoard)) {
            return reverseOneColumnize(solutionBoard, Board.getSize(), Board.getSize());
        }

        return null;
    }


    // Function for Board.java file
    public static boolean isSolvable() {
        return solvePuzzle() != null;
    }



    // Check if the board is solvable
    public static boolean isSolvable(MultipleBoard comboBoardRREF, MultipleBoard solutionBoard) {
        // The board is solvable if there is no row in the RREF of comboBoard
        // has all light off but the solution board in one column format
        // at that row has light on
        // e.g: [1 0 0 0 | 1]
        //      [0 1 0 0 | 1]
        //      [0 0 0 1 | 0]
        //      [0 0 0 0 | 1]
        //      -> unsolvable since the augmented matrix is inconsistent (has no solution)

        // A counter to keep track the number of lights off in a row
        int numLightOffInARow = 0;
        for (int row = 0; row < comboBoardRREF.getLengthSize(); row++) {
            for (int col = 0; col < comboBoardRREF.getWidthSize(); col++) {
                // If the light is not on
                if (!comboBoardRREF.getBoard(row, col).isLightOn()) {
                    // Increase the counter for the number of lights off for that row
                    numLightOffInARow++;
                }
            }

            // If the total number of lights off in a row equals the widthSize
            // (the number of columns), then that row has all lights off
            if (numLightOffInARow == comboBoardRREF.getWidthSize()) {
                // If the corresponding row of the "solution" matrix is light on
                if (solutionBoard.getBoard(row, 0).isLightOn()) {
                    // Then the board is unsolvable
                    return false;
                }
                // If the corresponding row of the "solution" matrix is light off
                // Then we continue to search for the next row
            }

            // Reset the counter for the number of lights off in a row
            numLightOffInARow = 0;
        }
        return true;
    }


    // Turn the m x n board into an (mxn) x 1 board (a one column board)
    public static MultipleBoard oneColumnize(MultipleBoard board) {
        int rowInc = 0;
        MultipleBoard oneColumnBoard = new MultipleBoard(board.getLengthSize() * board.getWidthSize(), 1);
        for (int row = 0; row < board.getLengthSize(); row++) {
            for (int col = 0; col < board.getWidthSize(); col++) {
                oneColumnBoard.setBoard(rowInc++, 0, board.getBoard(row, col).getLightState());
            }
        }
        return oneColumnBoard;
    }


    // Turn an (mxn) x 1 board to m x n board
    public static MultipleBoard reverseOneColumnize(MultipleBoard board, int newBoardLength, int newBoardWidth) {
        MultipleBoard revOneColBoard = new MultipleBoard(newBoardLength, newBoardWidth);
        for (int row = 0; row < board.getLengthSize(); row++) {
            revOneColBoard.setBoard(row / newBoardWidth, row % newBoardWidth,
                                    board.getBoard(row, 0).getLightState());
        }
        return revOneColBoard;
    }


    // Reverse all the lights of the board
    public static MultipleBoard reverseLightBoard(MultipleBoard board) {
        MultipleBoard revLightBoard = new MultipleBoard(board.getLengthSize(), board.getWidthSize());
        for (int row = 0; row < board.getLengthSize(); row++) {
            for (int col = 0; col < board.getWidthSize(); col++) {
                revLightBoard.setBoard(row, col, !board.getBoard(row, col).getLightState());
            }
        }
        return revLightBoard;
    }


    // Binary Reduced Row Echelon
    public static MultipleBoard binaryRREF(MultipleBoard givenBoard) {
        // Copy the givenBoard into board
        MultipleBoard board = new MultipleBoard(givenBoard.getLengthSize(), givenBoard.getWidthSize());
        for (int row = 0; row < givenBoard.getLengthSize(); row++) {
            for (int col = 0; col < givenBoard.getWidthSize(); col++) {
                board.setBoard(row, col, givenBoard.getBoard(row, col).getLightState());
            }
        }

        int currentRow = 0;
        int currentCol = 0;

        while (currentRow < board.getLengthSize() && currentCol < board.getWidthSize()) {
            // If the pivot tile is not light on
            while (currentCol < board.getWidthSize() && 
                   !board.getBoard(currentRow, currentCol).isLightOn()) {
                // Find in the later rows where that column is 1 (light on)
                for (int laterRow = currentRow + 1; laterRow < board.getLengthSize(); laterRow++) {
                    // If we found the wanted row
                    if (board.getBoard(laterRow, currentCol).isLightOn()) {
                        // Swap the row "row" with the row "laterRow"
                        for (int col = 0; col < board.getWidthSize(); col++) {
                            boolean temp = board.getBoard(currentRow, col).getLightState();
                            board.setBoard(currentRow, col, board.getBoard(laterRow, col).getLightState());
                            board.setBoard(laterRow, col, temp);
                        }
                        // Break the loop since we don't need to find anymore
                        break;
                    }
                }

                // If the pivot tile is still not light on after finding in later rows,
                // it means the whole column starting from currentRow down is not light on
                if (!board.getBoard(currentRow, currentCol).isLightOn()) {
                    // Move to the next column
                    currentCol++;
                } else {
                    // If the pivot tile is light on, break the loop
                    break;
                }                
            }

            // If we pass the end of the column, then break the loop
            if (currentCol >= board.getWidthSize())
                break;

            // Xor the all other rows whose currentCol is 1
            // to make only the currentRow has 1 (light on) at currentCol
            for (int otherRow = 0; otherRow < board.getLengthSize(); otherRow++) {
                if (otherRow != currentRow && board.getBoard(otherRow, currentCol).isLightOn()) {
                    for (int col = 0; col < board.getWidthSize(); col++) {
                        board.setBoard(otherRow, col, 
                                       board.getBoard(otherRow, col).getLightState() 
                                       ^ board.getBoard(currentRow, col).getLightState());
                    }
                }
            }

            // Increase currentRow
            currentRow++;
        }

        return board;
    }


    // This function perform the RREF on matrix A and do the same operation on each step
    // to matrix B. This is similar to solving an augmented matrix
    public static MultipleBoard[] binaryRREFTwoMatrices(MultipleBoard board1, MultipleBoard board2) {

        // Copy board1 to boardA
        MultipleBoard boardA = new MultipleBoard(board1.getLengthSize(), board1.getWidthSize());
        for (int row = 0; row < board1.getLengthSize(); row++) {
            for (int col = 0; col <board1.getWidthSize(); col++) {
                boardA.setBoard(row, col, board1.getBoard(row, col).getLightState());
            }
        }
        // Copy board2 to boardB
        MultipleBoard boardB = new MultipleBoard(board2.getLengthSize(), board2.getWidthSize());
        for (int row = 0; row < board2.getLengthSize(); row++) {
            for (int col = 0; col < board2.getWidthSize(); col++) {
                boardB.setBoard(row, col, board2.getBoard(row, col).getLightState());
            }
        }

        // Perform RREF
        int currentRow = 0;
        int currentCol = 0;

        while (currentRow < boardA.getLengthSize() && currentCol < boardA.getWidthSize()) {
            // If the pivot tile is not light on
            while (currentCol < boardA.getWidthSize() && 
                   !boardA.getBoard(currentRow, currentCol).isLightOn()) {
                // Find in the later rows where that column is 1 (light on)
                for (int laterRow = currentRow + 1; laterRow < boardA.getLengthSize(); laterRow++) {
                    // If we found the wanted row
                    if (boardA.getBoard(laterRow, currentCol).isLightOn()) {
                        // Swap the row "row" with the row "laterRow"
                        for (int col = 0; col < boardA.getWidthSize(); col++) {
                            boolean temp = boardA.getBoard(currentRow, col).getLightState();
                            boardA.setBoard(currentRow, col, boardA.getBoard(laterRow, col).getLightState());
                            boardA.setBoard(laterRow, col, temp);
                        }
                        // Do the same for board (matrix) B
                        for (int col = 0; col < boardB.getWidthSize(); col++) {
                            boolean temp = boardB.getBoard(currentRow, col).getLightState();
                            boardB.setBoard(currentRow, col, boardB.getBoard(laterRow, col).getLightState());
                            boardB.setBoard(laterRow, col, temp);
                        }
                        // Break the loop since we don't need to find anymore
                        break;
                    }
                }

                // If the pivot tile is still not light on after finding and swapping in later rows,
                // it means the whole column starting from currentRow down is not light on
                if (!boardA.getBoard(currentRow, currentCol).isLightOn()) {
                    // Move to the next column
                    currentCol++;
                } else {
                    // If the pivot tile is light on, break the loop
                    break;
                }                
            }

            // If we pass the end of the column, then break the loop
            if (currentCol >= boardA.getWidthSize())
                break;

            // XOR all other rows whose currentCol is 1
            // to make only the currentRow has 1 (light on) at currentCol
            // XOR operation in Java is ^
            for (int otherRow = 0; otherRow < boardA.getLengthSize(); otherRow++) {
                if (otherRow != currentRow && boardA.getBoard(otherRow, currentCol).isLightOn()) {
                    for (int col = 0; col < boardA.getWidthSize(); col++) {
                        boardA.setBoard(otherRow, col, 
                                        boardA.getBoard(otherRow, col).getLightState() 
                                        ^ boardA.getBoard(currentRow, col).getLightState());
                    }
                    // Do the same operation for matrix B
                    for (int col = 0; col < boardB.getWidthSize(); col++) {
                        boardB.setBoard(otherRow, col, 
                                        boardB.getBoard(otherRow, col).getLightState() 
                                        ^ boardB.getBoard(currentRow, col).getLightState());
                    }
                }
            }

            // Increase currentRow
            currentRow++;
        }

        return new MultipleBoard[] {boardA, boardB};
    }


    // Print the board
    private static void printBoard(MultipleBoard board) {
        for (int row = 0; row < board.getLengthSize(); row++) {
            for (int col = 0; col < board.getWidthSize(); col++) {
                System.out.print(board.getBoard(row, col).isLightOn() ? "1 " : "0 ");
            }
            System.out.println();
        }
    }
}
