package DDO_Solver;

import src.Board;
import src.Tile;

public class TestingSolver {
    public static void main(String[] args) {
        // Test the Solver
        Board.init(4);

        // Draw the Board out
        for (int row = 0; row < Board.getSize(); row++) {
            for (int col = 0; col < Board.getSize(); col++) {
                Tile tile = Board.getBoard(row, col);
                if (tile.getLightState())
                    System.out.print("1 ");
                else
                    System.out.print("0 ");
            }
            System.out.println();
        }

        // Solve the puzzle
        MultipleBoard solvedBoard = Solver.solvePuzzle();
        System.out.println();
        printBoard(solvedBoard);

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
        printBoard(Solver.oneColumnize(testBoard));
    }


    private static void testRevLight(int boardSize) {
        MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);
        testBoard.generateRandomBoard();
        System.out.println();
        System.out.println("Original Board:");
        printBoard(testBoard);
        System.out.println();
        System.out.println("Reversed Light Board:");
        printBoard(Solver.reverseLightBoard(testBoard));
    }


    private static void testOneColumnizeRevLight(int boardSize) {
        MultipleBoard testBoard = new MultipleBoard(boardSize, boardSize);
        testBoard.generateRandomBoard();
        System.out.println();
        System.out.println("Original Board:");
        printBoard(testBoard);
        System.out.println();
        System.out.println("OneColumnize Reversed Light Board:");
        printBoard(Solver.oneColumnize(Solver.reverseLightBoard(testBoard)));
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
        Solver.binaryRREFTwoMatrices(testBoard, testBoard2);
        System.out.println("Board A:");
        printBoard(testBoard);
        System.out.println("Board B:");
        printBoard(testBoard2);   
    }


    // Print the board
    private static void printBoard(MultipleBoard board) {
        if (board == null) {
            System.out.println("NULL");
            return;
        }
        for (int row = 0; row < board.getLengthSize(); row++) {
            for (int col = 0; col < board.getWidthSize(); col++) {
                System.out.print(board.getBoard(row, col).isLightOn() ? "1 " : "0 ");
            }
            System.out.println();
        }
    }    
}
