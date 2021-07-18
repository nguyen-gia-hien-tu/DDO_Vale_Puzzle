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


        // // Test oneColumnize function
        // testOneColumnize(boardSize);

        // // Test revLightBoard function
        // testRevLight(boardSize);
        
        // // Test oneColumnized(revLightBoard)
        // testOneColumnizeRevLight(boardSize);


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
}
