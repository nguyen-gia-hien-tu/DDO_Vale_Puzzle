package src;

/**
 * Controller
 */
public class Controller {
    public static void main(String[] args) {
        Board.init(3);
        new GUI();


        // Link how to binary RREF
        // https://stackoverflow.com/questions/14637478/how-can-i-find-a-solution-of-binary-matrix-equation-ax-b


        // ************* Terminal Graphic ***************
        // Scanner sc = new Scanner(System.in);

        // // Initialize the board
        // Board.init(3);
        // System.out.println("Initial Board");
        // View.drawBoard();

        // // Play the game
        // while (true) {
        //     if (Board.allTilesOn()) {
        //         System.out.println("\nYOU WON!!! WOOHOO");
        //         break;
        //     }

        //     // Take input from 0 to 8
        //     System.out.println();
        //     int input = sc.nextInt();

        //     // Play the game when the tile is clicked from the input
        //     Board.tileIsClicked(input / 3, input % 3);

        //     // Draw the board after the tile is clicked
        //     System.out.println();
        //     System.out.println("Board after Clicking Tile:");
        //     View.drawBoard();
        // }

        // sc.close();  
    }
}