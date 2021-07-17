package src;

/**
 * View
 */
public class View {
    public static void drawBoard() {
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
    }
    
}