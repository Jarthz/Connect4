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
    private WelcomeScreen welcomeScreen;
    private GameScreen gameScreen;
    private WinnerScreen winnerScreen;
    private ColumnFull columnFull;



    //contructor, create a board object as param for this class
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

        //same logic as before that we can call the actionperformed method as it's abstract of the interface directly by passing this class as a parameter and a lambdas do method
        ((WelcomeScreen) currentPanel).setButtonListeners(e -> welcomeActions(e));

    }

    /*wanted to do something fun and create a halloween special board object. Problem is that GUI has default colour white as a non static so each subclass instance inherits the original definition

    i could make the GUI interface static and then update the default colour
    BUT i wanted to run two concurrent games, one with the default white and one with haloween
    in hindsight then, GUI could instead not be an abstract class and should have a static modifier for default colour. I could create it as an object for each gamecontroller instance

    But instead, its staying abstract and I need to modify the object instance that inherits it. The problem is because of the flow of operations, i have to inject in the middle of the program the default colour scheme
    I don't create the object in question until many methods in

    so i've done some overloading, passing from main the default colour through the methods until it gets to where it needs to be

     */

    //halloween overloading
    public void start(Color defaultColour){
        currentPanel = new WelcomeScreen();
        currentPanel.setController(this);
        currentPanel.showScreen();
        ((WelcomeScreen) currentPanel).setButtonListeners(e -> welcomeActions(e, defaultColour));

    }

    public void startGame(){
        removeFrame();
        currentPanel = new GameScreen(gameBoard);
        currentPanel.setController(this);
        currentPanel.showScreen();
    }

    //overloading
    public void startGame(Color defaultColour){
        removeFrame();
        currentPanel = new GameScreen(gameBoard);
        currentPanel.setController(this);
        currentPanel.setDefaultColour(defaultColour);
        currentPanel.showScreen();
    }


    public void removeColourScheme(){
        currentPanel.removeAllColours();
    }
    public void addColourScheme(Color colour){
        currentPanel.addColour(colour);
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
            welcomeDefensiveBlockers();
        } else if (command.equals("twoPlayers")) {
            setupPlayers(2, false);
            welcomeDefensiveBlockers();
        } else if(command.equals("start")){
            startGame();
        }

    }

    //haloween overloading
    public void welcomeActions(ActionEvent e, Color defaultColour){
        String command = e.getActionCommand();
        if(command.equals("onePlayer")){
            setupPlayers(1, true);
            welcomeDefensiveBlockers();
        } else if (command.equals("twoPlayers")) {
            setupPlayers(2, false);
            welcomeDefensiveBlockers();
        } else if(command.equals("start")){
            startGame(defaultColour);
        }
    }

    //error handling so you cannot start a game without players or spam build player button
    //start game is disabled by default
    public void welcomeDefensiveBlockers(){
        currentPanel.disableButton(currentPanel.getPlayerButtons());
        currentPanel.enableButton(currentPanel.getStartButton());
    }



    public void dropToken(int column) {
        JButton[][] gridButtons = currentPanel.getGridButtons();
        int row = gridButtons.length;
        System.out.println(currentPlayer);

        //boardfull check
        Predicate<JButton> isNotFull = button -> button.getBackground() == currentPanel.getDefaultColour();
        boolean boardFull = gameBoard.isBoardFull(gridButtons, isNotFull);

        if(boardFull){
            showBoardFull();
        } else {

            //lambda to handle token placement for each player. It'll check for the lowest row to place the token based on the column parameter
            BiConsumer<Integer, Color> placeToken = (col, color) -> {
                if (gridButtons[0][col].getBackground() == currentPanel.getDefaultColour()) {
                    for (int r = row - 1; r >= 0; r--) {
                        if (gridButtons[r][col].getBackground() == currentPanel.getDefaultColour()) {
                            gridButtons[r][col].setBackground(color);
                            switchTurns();
                            break;
                        }
                    }

                    //create logic here for the AI to see what the current full column is and choose another
                } else if(!(currentPlayer instanceof Player.AI)){
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

    public boolean isColumnFull(int column){
        JButton[][] gridButtons = currentPanel.getGridButtons();
        if(gridButtons[0][column].getBackground() == currentPanel.getDefaultColour()){
            return false;
        }
        return true;
    }


    //change the current player
    public void switchTurns() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
    }


//change this to a block consumer logic to handle print message
    public boolean isGameOver(){
        gameOverCheck(
                p -> checkWinner(p.getColour())
        );
        return true;
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
