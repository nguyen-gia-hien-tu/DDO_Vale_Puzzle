package src;


/**
 * Model Board
 */
public class Board {
    private static int size;
    private static Tile[][] board;
    private static int moves;

    public static void init(int boardSize) {
        if (boardSize <= 2) {
            throw new IllegalArgumentException();
        }

        // Set the size of the board
        Board.size = boardSize;
        // Generate the board array of Tile
        Board.board = new Tile[Board.size][Board.size];
        // Generate move to 0
        Board.moves = 0;

        // Randomly generate each tile to be on or off
        do {
            Board.generateBoard();
        } while (Board.allTilesOn());
        
    }


    // Setter
    // setSize is not allowed as we cannot change the size of the board
    // once we have already init the board
    public static void setMoves(int newMoves) {
        Board.moves = newMoves;
    }
    
    public static void setBoard(int r, int c, boolean state) {
        Board.board[r][c].setLightState(state);
    }


    // Getters
    public static int getSize() {
        return Board.size;
    }

    public static Tile getBoard(int row, int col) {
        return Board.board[row][col];
    }

    public static int getMoves() {
        return Board.moves;
    }


    // Increase the number of moves
    public static void incMoves() {
        Board.moves++;
    }


    // Check if the board is solved, i.e., all the tiles' lights are on
    public static boolean allTilesOn() {
        for (int row = 0; row < Board.size; row++)
            for (int col = 0; col < Board.board[row].length; col++)
                if (!Board.board[row][col].isLightOn())
                    return false;

        return true;
    }


    // Process when the tile is clicked
    public static void tileIsClicked(int r, int c) {
        Board.flipLight(r, c);

        // If the clicked tile is on the first row of the board
        if (r == 0) {
            // If the clicked tile is the top left corner of the board
            if (c == 0) {
                // Flip the lights of the tiles that are adjacent right and 
                // adjacent below of the current tile
                Board.flipLight(r, c + 1);
                Board.flipLight(r + 1, c);
            }
            // If the clicked tile is the top right corner of the board
            else if (c == Board.board[r].length - 1) {
                Board.flipLight(r, c - 1);
                Board.flipLight(r + 1, c);
            }
            // If the clicked tile is on the first row and not the top left
            // nor top right corner of the board
            else {
                Board.flipLight(r, c - 1);
                Board.flipLight(r, c + 1);
                Board.flipLight(r + 1, c);
            }
        }
        // If the clicked tile is on the last row of the board
        else if (r == Board.size - 1) {
            // If the clicked tile is the bottom left corner of the board
            if (c == 0) {
                // Flip the lights of the tiles that are ajacent right and
                // adjacent top of the current tiles
                Board.flipLight(r, c + 1);
                Board.flipLight(r - 1, c);                
            }
            // If the clicked tile is the bottom right corner of the board
            else if (c == Board.board[r].length - 1) {
                Board.flipLight(r, c - 1);
                Board.flipLight(r - 1, c);
            }
            // If the clicked tile is on the last row but not the bottom left
            // not bottom right corner of the board
            else {
                Board.flipLight(r, c - 1);
                Board.flipLight(r, c + 1);
                Board.flipLight(r - 1, c);
            }
        }
        // If the clicked tile is not on the first nor the last row of the board
        else {
            // And if the clicked tile is on the first column of the board
            if (c == 0) {
                Board.flipLight(r, c + 1);
                Board.flipLight(r - 1, c);
                Board.flipLight(r + 1, c);
            }
            // And else if the clicked tile is on the last column of the board
            else if (c == Board.board[r].length - 1) {
                Board.flipLight(r, c - 1);
                Board.flipLight(r - 1, c);
                Board.flipLight(r + 1, c);
            }
            // And else if the clicked tile is not on the first column nor last
            // column of the board
            else {
                Board.flipLight(r, c - 1);
                Board.flipLight(r, c + 1);
                Board.flipLight(r - 1, c);
                Board.flipLight(r + 1, c);
            }
        }
    }


    public static void flipLight(int r, int c) {
        Tile tile = Board.board[r][c];
        tile.setLightState(!tile.getLightState());
    }


    // Private Auxiliary Functions
    private static void generateBoard() {
        for (int row = 0; row < Board.size; row++) {
            for (int col = 0; col < Board.board[row].length; col++) {                
                if ((int) (Math.random() * 2) == 0)
                    Board.board[row][col] = new Tile(false);
                else
                    Board.board[row][col] = new Tile(true);
            }
        }
    }

    
}