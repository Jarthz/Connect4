

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;


public class GameController {


    private Player currentPlayer;
    private List<Player> players;
    private int currentPlayerIndex = 0;
    private GUI currentPanel;
    private Board gameBoard;

    //
    public GameController(Board gameBoard) {
        this.gameBoard = gameBoard;

    }

    public void recordWin(){
        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.incrementScore();
    }

    public void start(){
        currentPanel = new WelcomeScreen();
        currentPanel.setController(this);
        currentPanel.showScreen();

        //same logic as before that we can call the actionperformed method as it's abstract of the interface directly by passing this class as a parameter
        ((WelcomeScreen) currentPanel).setButtonListeners(e -> welcomeActions(e));

    }

    public void startGame(){
        removeFrame();
        currentPanel = new GameScreen(gameBoard);
        currentPanel.setController(this);
        currentPanel.showScreen();
    }

    public void showWinner(String winner){
        removeFrame();
        currentPanel = new WinnerScreen(winner);
        currentPanel.setController(this);
        currentPanel.showScreen();
        start();
    }

    public void showColumnFull(){
        GUI fullPanel = new ColumnFull();
        fullPanel.showScreen();
    }

    public void showBoardFull(){
        removeFrame();
        GUI fullPanel = new ColumnFull.BoardFull();
        fullPanel.showScreen();
        start();
    }


    public void setupPlayers(int numberOfPlayers, boolean AI){
        players = Player.setupPlayers(numberOfPlayers, AI, currentPanel.getColours());
        currentPlayerIndex = 0;
        currentPlayer = players.get(currentPlayerIndex);

    }

    public void removeFrame(){
        JFrame frame = currentPanel.getFrame();
        frame.dispose();

    }

    public void welcomeActions(ActionEvent e){
        String command = e.getActionCommand();

        if(command.equals("onePlayer")){
            setupPlayers(1, true);
            currentPanel.disableButton(currentPanel.getPlayerButtons());
            currentPanel.enableButton(currentPanel.getStartButton());
        } else if (command.equals("twoPlayers")) {
            setupPlayers(2, false);
            currentPanel.disableButton(currentPanel.getPlayerButtons());
            currentPanel.enableButton(currentPanel.getStartButton());
        } else if(command.equals("start")){
            startGame();
        }

    }



    public void dropToken(int column) {
        JButton[][] gridButtons = currentPanel.getGridButtons();
        int row = gridButtons.length;
        System.out.println(currentPlayer);

        //boardfull check
        Predicate<JButton> isNotFull = button -> button.getBackground() == Color.WHITE;
        boolean boardFull = gameBoard.isBoardFull(gridButtons, isNotFull);

        if(boardFull){
            showBoardFull();
        } else {

            //lambda to handle token placement for each player. It'll check for the lowest row to place the token based on the column parameter
            BiConsumer<Integer, Color> placeToken = (col, color) -> {
                if (gridButtons[0][col].getBackground() == Color.WHITE) {
                    for (int r = row - 1; r >= 0; r--) {
                        if (gridButtons[r][col].getBackground() == Color.WHITE) {
                            gridButtons[r][col].setBackground(color);
                            switchTurns();
                            break;
                        }
                    }
                } else {
                    showColumnFull();
                }

            };

            //human move calls the lambda and parses the paramater column obtained from the action listener and colour
            if (!(currentPlayer instanceof Player.AI)) {
                placeToken.accept(column, currentPlayer.getColour());
            }


            //'AI' move (yellow)
            //call the makeAIMove method from the subclass and return the value to an int effictive final
            if (currentPlayer instanceof Player.AI) {
                int columnAI = ((Player.AI) currentPlayer).makeAIMove(gridButtons[0].length);
                //lambda to now place this token based on the column
                placeToken.accept(columnAI, currentPlayer.getColour());

                //TO DO add some logic that the AI automatically doesn't true to drop into a full column
            }

            isGameOver();
        }
    }


    //change the current player
    public void switchTurns() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
    }


//refactor this so that it doesn't call the screen in this loop
    public void gameOverCheck(
            Predicate<Player> playerPredicate){
            for (Player p : players){
                if(playerPredicate.test (p)) {
                    String message = "Winner is " + p.toString();
                    showWinner(message);
                    return;
                }
            }

    }


    public boolean isGameOver(){
        gameOverCheck(
                p -> checkWinner(p.getColour())
        );
        return true;
    }

    public void gameOverCheck2(Predicate<Player> playerPredicate){
        if(playerPredicate.test(currentPlayer)){
            System.out.println("game over 2 " + currentPlayer);
        }
    }

    public boolean gameOver2(){
        gameOverCheck2(
                p -> checkWinner(p.getColour())
        );
        return true;
    }


    public boolean checkWinner(Color playerColor) {
        JButton[][] gridButtons = currentPanel.getGridButtons();
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
        JButton[][] gridButtons = currentPanel.getGridButtons();
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
