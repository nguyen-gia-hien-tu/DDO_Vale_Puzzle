package DDO_Solver;

public class Solver {
    public static void main(String[] args) {
        int boardSize = 3;
        MultipleBoard[] oneTileClickedBoards = new MultipleBoard[boardSize * boardSize];
        MultipleBoard comboBoard = new MultipleBoard(boardSize * boardSize, boardSize * boardSize);

        // Print out all the boards with one tile is clicked
        System.out.println("\nBoards with One Tile Clicked:");
        for (int i = 0; i < oneTileClickedBoards.length; i++) {
            oneTileClickedBoards[i] = new MultipleBoard(boardSize, boardSize);
            oneTileClickedBoards[i].flipCurrAndAdjLights(i / boardSize, i % boardSize);
            printBoard(oneTileClickedBoards[i]);
            System.out.println();
        }

        // To create comboBoard, the numbers at position [row][col]
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

        
        // Test Board (Initial Board)
        MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);
        // [0 1 0]
        // [1 1 0]
        // [0 1 1]
        testBoard.setBoard(0, 1, true);
        testBoard.setBoard(1, 0, true);
        testBoard.setBoard(1, 1, true);
        testBoard.setBoard(2, 1, true);
        testBoard.setBoard(2, 2, true);
        System.out.println("\nOriginal Board:");
        printBoard(testBoard);

        MultipleBoard revLightBoard = reverseLightBoard(testBoard);
        MultipleBoard oneColumnBoard = oneColumnize(testBoard);
        MultipleBoard oneColumnRevLightBoard = oneColumnize(revLightBoard);
        System.out.println();
        // System.out.println("One Column Light Board:");
        // printBoard(oneColumnBoard);
        System.out.println("One Colum Reverse Light Board:");
        printBoard(oneColumnRevLightBoard);

        binaryRREFTwoMatrices(comboBoard, oneColumnBoard);
        // System.out.println("One Column Board After RREF:");
        // printBoard(oneColumnBoard);

        System.out.println("One Column Reverse Light Board After RREF:");
        printBoard(oneColumnRevLightBoard);



        // // Test oneColumnize function
        // testOneColumnize(boardSize);

        // // Test revLightBoard function
        // testRevLight(boardSize);
        
        // // Test oneColumnized(revLightBoard)
        // testOneColumnizeRevLight(boardSize);

        // // Test binaryRREF function
        // testBinaryRREF(boardSize);

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
        }
    }


    private static void binaryRREFTwoMatrices(MultipleBoard boardA, MultipleBoard boardB) {
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
    }


    private static void testBinaryRREF(int boardSize) {
        MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);

        // [1 0 1]
        // [0 0 1]
        // [1 1 1]
        // testBoard.setBoard(0, 0, true);
        // testBoard.setBoard(0, 2, true);
        // testBoard.setBoard(1, 2, true);
        // testBoard.setBoard(2, 0, true);
        // testBoard.setBoard(2, 1, true);
        // testBoard.setBoard(2, 2, true);
        // System.out.println("\nOriginal Board:");
        // printBoard(testBoard);
        // System.out.println("\nBinary RREF Board:");
        // binaryRREF(testBoard);

        // [0 0 1]
        // [0 1 0]
        // [1 0 0]
        // testBoard = new MultipleBoard(boardSize, boardSize);
        // testBoard.setBoard(0, 2, true);
        // testBoard.setBoard(1, 1, true);
        // testBoard.setBoard(2, 0, true);
        // System.out.println("\nOriginal Board:");
        // printBoard(testBoard);
        // System.out.println("\nBinary RREF Board:");
        // binaryRREF(testBoard);

        // [1 1 0]
        // [0 1 0]
        // [0 1 1]
        // testBoard = new MultipleBoard(boardSize, boardSize);
        // testBoard.setBoard(0, 0, true);
        // testBoard.setBoard(0, 1, true);
        // testBoard.setBoard(1, 1, true);
        // testBoard.setBoard(2, 1, true);
        // testBoard.setBoard(2, 2, true);
        // System.out.println("\nOriginal Board:");
        // printBoard(testBoard);
        // System.out.println("\nBinary RREF Board:");
        // binaryRREF(testBoard);

        // [0 1 1 | 0]
        // [0 1 1 | 0]
        // [1 1 1 | 1]
        // testBoard = new MultipleBoard(boardSize, boardSize);
        // testBoard.setBoard(0, 1, true);
        // testBoard.setBoard(0, 2, true);
        // testBoard.setBoard(1, 1, true);
        // testBoard.setBoard(1, 2, true);
        // testBoard.setBoard(2, 0, true);
        // testBoard.setBoard(2, 1, true);
        // testBoard.setBoard(2, 2, true);

        // [1 0 0 | 0]
        // [0 1 1 | 0]
        // [1 1 0 | 1]
        testBoard = new MultipleBoard(boardSize, boardSize);
        testBoard.setBoard(0, 0, true);
        testBoard.setBoard(1, 1, true);
        testBoard.setBoard(1, 2, true);
        testBoard.setBoard(2, 0, true);
        testBoard.setBoard(2, 1, true);

        MultipleBoard testBoard2 = new MultipleBoard(3, 1);
        testBoard2.setBoard(2, 0, true);

        System.out.println("\nOriginal Board:");
        System.out.println("Board A:");
        printBoard(testBoard);
        System.out.println("Board B:");
        printBoard(testBoard2);
        System.out.println("\nBinary RREF Boards:");
        binaryRREFTwoMatrices(testBoard, testBoard2);
        System.out.println("Board A:");
        printBoard(testBoard);
        System.out.println("Board B:");
        printBoard(testBoard2);
        
    }

}
