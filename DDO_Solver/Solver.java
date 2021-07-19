package DDO_Solver;

public class Solver {
    private static MultipleBoard[] oneTileClickedBoards;
    private static MultipleBoard comboBoard;
    public static void main(String[] args) {
        int boardSize = 4;
        oneTileClickedBoards = new MultipleBoard[boardSize * boardSize];
        comboBoard = new MultipleBoard(boardSize * boardSize, boardSize * boardSize);

        // Print out all the boards with one tile is clicked
        System.out.println("\nBoards with One Tile Clicked:");
        for (int i = 0; i < oneTileClickedBoards.length; i++) {
            oneTileClickedBoards[i] = new MultipleBoard(boardSize, boardSize);
            oneTileClickedBoards[i].flipCurrAndAdjLights(i / boardSize, i % boardSize);
            // printBoard(oneTileClickedBoards[i]);
            // System.out.println();
        }

        // To create comboBoard, the light state at position [row][col]
        // (where 0 <= row < boardSize, 0 <= col < boardSize)
        // of each board in the oneTileClickedBoards
        // will form the row (boardSize * row + col) of the comboBoard
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                for (int i = 0; i < oneTileClickedBoards.length; i++) {
                    comboBoard.setBoard(boardSize * row + col, i,
                                        oneTileClickedBoards[i].getBoard(row, col).getLightState());
                }
            }
        }
        System.out.println();
        System.out.println("Combo Board:");
        printBoard(comboBoard);

        // // comboBoard after RREF
        // binaryRREF(comboBoard);
        // System.out.println("\nCombo Board After RREF:");
        // printBoard(comboBoard);
        
        // // Test Board (Initial Board)
        // MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);
        // // [0 1 0]
        // // [1 1 0]
        // // [0 1 1]
        // testBoard.setBoard(0, 1, true);
        // testBoard.setBoard(1, 0, true);
        // testBoard.setBoard(1, 1, true);
        // testBoard.setBoard(2, 1, true);
        // testBoard.setBoard(2, 2, true);
        // System.out.println("\nOriginal Board:");
        // printBoard(testBoard);

        // MultipleBoard revLightBoard = reverseLightBoard(testBoard);
        // MultipleBoard oneColumnBoard = oneColumnize(testBoard);
        // MultipleBoard oneColumnRevLightBoard = oneColumnize(revLightBoard);
        // System.out.println();
        // // System.out.println("One Column Light Board:");
        // // printBoard(oneColumnBoard);
        // System.out.println("One Colum Reverse Light Board:");
        // printBoard(oneColumnRevLightBoard);

        // // binaryRREFTwoMatrices(comboBoard, oneColumnBoard);
        // // System.out.println("One Column Board After RREF:");
        // // printBoard(oneColumnBoard);

        // binaryRREFTwoMatrices(comboBoard, oneColumnRevLightBoard);
        // System.out.println("One Column Reverse Light Board After RREF of Combo Board:");
        // printBoard(oneColumnRevLightBoard);

        // MultipleBoard revOneColRevLightBoard = reverseOneColumnize(oneColumnRevLightBoard, boardSize, boardSize);
        // System.out.println("\nReverse One Column Reverse Light Board After RREF of Combo Board:");
        // printBoard(revOneColRevLightBoard);


        // Test Board 4 x 4
        MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);
        testBoard.setBoard(0, 0, true); 
        testBoard.setBoard(0, 1, true);
        testBoard.setBoard(1, 0, true);
        testBoard.setBoard(2, 1, true);
        testBoard.setBoard(2, 2, true);
        testBoard.setBoard(3, 0, true);
        testBoard.setBoard(3, 3, true);
        System.out.println("\nOriginal Board:");
        printBoard(testBoard);
        MultipleBoard oneColRevLightBoard = oneColumnize(reverseLightBoard(testBoard));
        binaryRREFTwoMatrices(comboBoard, oneColRevLightBoard);
        System.out.println("\nSolution:");
        printBoard(reverseOneColumnize(oneColRevLightBoard, 4, 4));




        // // Test oneColumnize function
        // testOneColumnize(boardSize);

        // // Test revLightBoard function
        // testRevLight(boardSize);
        
        // // Test oneColumnized(revLightBoard)
        // testOneColumnizeRevLight(boardSize);

        // // Test binaryRREF function
        // testBinaryRREF(boardSize);

    }


    // Implementing solver analysis
    private static boolean isSolvable() {
        // The board is solvable if there is no row in the RREF of comboBoard
        // has all light off but the solution board in one column format
        // at that row has light on
        // e.g: [1 0 0 0 | 1]
        //      [0 1 0 0 | 1]
        //      [0 0 0 1 | 0]
        //      [0 0 0 0 | 1]
        //      -> unsolvable since the augmented matrix has no solution
        // Prerequisite 
        return true;
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
        // Return an array of two matrices
        MultipleBoard[] resultBoards = new MultipleBoard[2];
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

                // If the pivot tile is still not light on after finding in later rows,
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

            // Xor the all other rows whose currentCol is 1
            // to make only the currentRow has 1 (light on) at currentCol
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

        return resultBoards;
    }
}
