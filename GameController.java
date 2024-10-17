import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.BiConsumer;

public class GameController {

    private List<Player> players;
    private boolean gameOver;
    private Connect4GUI gui;
    private int currentPlayerIndex;

    //
    public GameController(Connect4GUI gui) {
        this.gui = gui;
        this.gameOver = false;
        this.players = new ArrayList<>()
    }

    public void initialisePlayers(List<Player> players) {
        this.players = players;
        this.currentPlayerIndex = 0;

    }


    public void dropToken(int column) {
        JButton[][] gridButtons = gui.getGridButtons();
        int row = gridButtons.length;

        //lambda to handle token placement for each player. It'll check for the lowest row to place the token based on the column parameter
        BiConsumer<Integer, Color> placeToken = (col, color) -> {
            for(int r = row - 1; r >=0; r--){
                if(gridButtons[r][col].getBackground() == Color.WHITE){
                    gridButtons[r][col].setBackground(color);
                    break;
                }
            }
        };

        //human move calls the lamber and parses the params column from the action listener and human red colour

        if(!(currentPlayer instanceof Player.AI)) {
            placeToken.accept(column, player1.colour);
        }

        //'AI' move (yellow)
        //call the makeAIMove method from the subclass and return the value to an int effictive final
        else {
            int columnAI = ((Player.AI) player2).makeAIMove(gridButtons[0].length);
            //lambda to now place this token based on the column
            placeToken.accept(columnAI, player2.colour);
        }
        if(gameOver()){
            return;
        }


        // Switch turns after both moves
        switchTurns();
    }


    //change the current player
    public void switchTurns() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }


    public interface GameOverCheck{
        boolean check(Player player);
    }



    GameOverCheck gameOverCheck = (player) -> {
        if (checkWinner(player.colour)) {
            System.out.println("Winner is " + player);
            return true;
        }
        return false;
    };


    public boolean gameOver(){
        return gameOverCheck.check(player1) || gameOverCheck.check(player2);
    }

    public boolean checkWinner(Color playerColor) {
        JButton[][] gridButtons = gui.getGridButtons();
        int rows = gridButtons.length;
        int columns = gridButtons[0].length;

        // Define the directions to check: right, down, down-right diagonal, and down-left diagonal
        int[][] directions = {
                {0, 1},   // Right (horizontal)
                {1, 0},   // Down (vertical)
                {1, 1},   // Down-right diagonal
                {1, -1}   // Down-left diagonal
        };

        // Loop through all grid positions
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Check if the current button matches the player's color
                if (gridButtons[row][col].getBackground() == playerColor) {
                    // Check all 4 possible directions
                    for (int[] direction : directions) {
                        if (checkDirection(row, col, direction[0], direction[1], playerColor)) {
                            return true;  // Winner found!
                        }
                    }
                }
            }
        }

        return false;  // No winner found
    }

    private boolean checkDirection(int row, int col, int rowDelta, int colDelta, Color playerColor) {
        JButton[][] gridButtons = gui.getGridButtons();
        int rows = gridButtons.length;
        int columns = gridButtons[0].length;

        // Check if there are 4 consecutive tokens in the given direction
        for (int i = 1; i < 4; i++) {
            int newRow = row + rowDelta * i;
            int newCol = col + colDelta * i;

            // Ensure the new position is within bounds
            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= columns) {
                return false;
            }

            // Check if the token at the new position matches the player's color
            if (gridButtons[newRow][newCol].getBackground() != playerColor) {
                return false;
            }
        }

        return true;  // 4 consecutive tokens found!
    }




}
