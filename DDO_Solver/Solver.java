package DDO_Solver;

public class Solver {
    public static void main(String[] args) {
        int boardSize = 3;
        MultipleBoard[] oneTileClickedBoards = new MultipleBoard[boardSize * boardSize];
        MultipleBoard comboBoard = new MultipleBoard(boardSize * boardSize, boardSize * boardSize);

        // // Print out all the boards with one tile is clicked
        // System.out.println("\nBoards with One Tile Clicked:");
        // for (int i = 0; i < oneTileClickedBoards.length; i++) {
        //     oneTileClickedBoards[i] = new MultipleBoard(boardSize, boardSize);
        //     oneTileClickedBoards[i].flipCurrAndAdjLights(i / boardSize, i % boardSize);
        //     printBoard(oneTileClickedBoards[i]);
        //     System.out.println();
        // }

        // // To create comboBoard, the numbers at position [row][col]
        // // (where 0 <= row < boardSize, 0 <= col < boardSize)
        // // of each board in the oneTileClickedBoards
        // // will form the row (boardSize * row + col) of the comboBoard
        // for (int row = 0; row < boardSize; row++) {
        //     for (int col = 0; col < boardSize; col++) {
        //         for (int i = 0; i < oneTileClickedBoards.length; i++) {
        //             comboBoard.setBoard(boardSize * row + col, i,
        //                                 oneTileClickedBoards[i].getBoard(row, col).getLightState());
        //         }
        //     }
        // }
        // System.out.println();
        // System.out.println("Combo Board:");
        // printBoard(comboBoard);


        // // Test oneColumnize function
        // testOneColumnize(boardSize);

        // // Test revLightBoard function
        // testRevLight(boardSize);
        
        // // Test oneColumnized(revLightBoard)
        // testOneColumnizeRevLight(boardSize);

        // Test binaryRREF function
        testBinaryRREF(boardSize);

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
    private static MultipleBoard oneColumnize(MultipleBoard board) {
        int rowInc = 0;
        MultipleBoard oneColumnBoard = new MultipleBoard(board.getLengthSize() * board.getWidthSize(), 1);
        for (int row = 0; row < board.getLengthSize(); row++) {
            for (int col = 0; col < board.getWidthSize(); col++) {
                oneColumnBoard.setBoard(rowInc++, 0, board.getBoard(row, col).getLightState());
            }
        }
        return oneColumnBoard;
    }

    private static void testOneColumnize(int boardSize) {
        // Turn a board into a one column board
        MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);
        testBoard.generateRandomBoard();
        System.out.println();
        System.out.println("Original Board:");
        printBoard(testBoard);
        System.out.println();
        System.out.println("One Columnized Board:");
        printBoard(oneColumnize(testBoard));
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

    private static void testRevLight(int boardSize) {
        MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);
        testBoard.generateRandomBoard();
        System.out.println();
        System.out.println("Original Board:");
        printBoard(testBoard);
        System.out.println();
        System.out.println("Reversed Light Board:");
        printBoard(reverseLightBoard(testBoard));
    }

    private static void testOneColumnizeRevLight(int boardSize) {
        MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);
        testBoard.generateRandomBoard();
        System.out.println();
        System.out.println("Original Board:");
        printBoard(testBoard);
        System.out.println();
        System.out.println("OneColumnize Reversed Light Board:");
        printBoard(oneColumnize(reverseLightBoard(testBoard)));
    }


    // Binary Reduced Row Echelon
    private static void binaryRREF(MultipleBoard board) {
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

            System.out.println();
            printBoard(board);
        }
    }

    private static void testBinaryRREF(int boardSize) {
        MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);
        testBoard.setBoard(0, 0, true);
        testBoard.setBoard(0, 2, true);
        testBoard.setBoard(1, 2, true);
        testBoard.setBoard(2, 0, true);
        testBoard.setBoard(2, 1, true);
        testBoard.setBoard(2, 2, true);
        System.out.println("\nOriginal Board:");
        printBoard(testBoard);
        System.out.println("\nBinary RREF Board:");
        binaryRREF(testBoard);
    }

}
