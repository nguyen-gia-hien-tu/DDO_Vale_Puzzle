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


    // Unused Function from Solver.java (moved to here)
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
}
