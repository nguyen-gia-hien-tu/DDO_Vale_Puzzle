package DDO_Solver;

import src.Tile;

public class MultipleBoard {

    private int lengthSize;
    private int widthSize;
    private Tile[][] board;
    private int moves;

    public MultipleBoard(int newLengthSize, int newWidthSize) {
        this.lengthSize = newLengthSize;
        this.widthSize = newWidthSize;
        this.board = new Tile[this.lengthSize][this.widthSize];
        generateBoardOfLightsOff();
        this.moves = 0;
    }

    // Setters
    public void setSize(int newLengthSize, int newWidthSize) {
        this.lengthSize = newLengthSize;
        this.widthSize = newWidthSize;
    }

    public void setBoard(int row, int col, boolean newLightState) {
        this.board[row][col].setLightState(newLightState);;
    }

    public void setMoves(int newMoves) {
        this.moves = newMoves;
    }


    // Getters
    public int getLengthSize() {
        return this.lengthSize;
    }

    public int getWidthSize() {
        return this.widthSize;
    }

    public Tile getBoard(int row, int col) {
        return this.board[row][col];
    }

    public int getMoves() {
        return this.moves;
    }


    // Flip the current light and the adjacent lights
    public void flipCurrAndAdjLights(int r, int c) {
        this.flipLight(r, c);

        // If the clicked tile is on the first row of the board
        if (r == 0) {
            // If the clicked tile is the top left corner of the board
            if (c == 0) {
                // Flip the lights of the tiles that are adjacent right and 
                // adjacent below of the current tile
                this.flipLight(r, c + 1);
                this.flipLight(r + 1, c);
            }
            // If the clicked tile is the top right corner of the board
            else if (c == this.board[r].length - 1) {
                this.flipLight(r, c - 1);
                this.flipLight(r + 1, c);
            }
            // If the clicked tile is on the first row and not the top left
            // nor top right corner of the board
            else {
                this.flipLight(r, c - 1);
                this.flipLight(r, c + 1);
                this.flipLight(r + 1, c);
            }
        }
        // If the clicked tile is on the last row of the board
        else if (r == this.lengthSize - 1) {
            // If the clicked tile is the bottom left corner of the board
            if (c == 0) {
                // Flip the lights of the tiles that are ajacent right and
                // adjacent top of the current tiles
                this.flipLight(r, c + 1);
                this.flipLight(r - 1, c);                
            }
            // If the clicked tile is the bottom right corner of the board
            else if (c == this.board[r].length - 1) {
                this.flipLight(r, c - 1);
                this.flipLight(r - 1, c);
            }
            // If the clicked tile is on the last row but not the bottom left
            // not bottom right corner of the board
            else {
                this.flipLight(r, c - 1);
                this.flipLight(r, c + 1);
                this.flipLight(r - 1, c);
            }
        }
        // If the clicked tile is not on the first nor the last row of the board
        else {
            // And if the clicked tile is on the first column of the board
            if (c == 0) {
                this.flipLight(r, c + 1);
                this.flipLight(r - 1, c);
                this.flipLight(r + 1, c);
            }
            // And else if the clicked tile is on the last column of the board
            else if (c == this.board[r].length - 1) {
                this.flipLight(r, c - 1);
                this.flipLight(r - 1, c);
                this.flipLight(r + 1, c);
            }
            // And else if the clicked tile is not on the first column nor last
            // column of the board
            else {
                this.flipLight(r, c - 1);
                this.flipLight(r, c + 1);
                this.flipLight(r - 1, c);
                this.flipLight(r + 1, c);
            }
        }
    }


    // Flip only the current light
    public void flipLight(int r, int c) {
        Tile tile = this.board[r][c];
        tile.setLightState(!tile.getLightState());
    }


    // Private method to generate the board of zero (light off)
    private void generateBoardOfLightsOff() {
        for (int row = 0; row < this.lengthSize; row++) {
            for (int col = 0; col < this.board[row].length; col++) {
                this.board[row][col] = new Tile(false);
            }
        }
    }

    // Auxiliary Function to generate random board
    public void generateRandomBoard() {
        for (int row = 0; row < this.lengthSize; row++) {
            for (int col = 0; col < this.board[row].length; col++) {                
                if ((int) (Math.random() * 2) == 0)
                    this.setBoard(row, col, false);
                else
                    this.setBoard(row, col, true);
            }
        }
    }
    
}
