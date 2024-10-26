//Controller class interacts with player input, GUI and game logic. It handles the execution flow.

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


    public GameController(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    //utility method when setting up new GUI subclass
    private void currentPanelInitialise(GUI panel){
        panel.setController(this);
        //abstract method of the subclass, all must have this method
        panel.showScreen();
    }

    //start the program on the welcome screen and handle player instantiation
    public void start(){
        currentPanel = new WelcomeScreen();
        currentPanelInitialise(currentPanel);

        //assign action listeners to the JButtons on the welcome screen using a method
        //welcomeAction method argument listens to the ActionPerformed object
        currentPanel.setButtonListeners(e -> welcomeActions(e));
    }

    //receive button event and then do action based on which button
    //either setup players + AI or start game
    private void welcomeActions(ActionEvent e){
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

    //pass the number of players and with AI from the controller to the Player call by invoking the player setupPlayers method
    private void setupPlayers(int numberOfPlayers, boolean AI){
        players = Player.setupPlayers(numberOfPlayers, AI, currentPanel.getColours());
        currentPlayerIndex = 0;
        currentPlayer = players.get(currentPlayerIndex);
    }

    //error handling so you cannot start a game without players or spam build player button
    //start game is disabled by default and needs enabling once players are created
    private void welcomeDefensiveBlockers(){
        currentPanel.disableButton(currentPanel.getPlayerButtons());
        currentPanel.enableButton(currentPanel.getStartButton());
    }

    //clear prior frame, create new GameScreen object and invoke the showFrame method through initialise
    private void startGame(){
        removeFrame();
        currentPanel = new GameScreen(gameBoard);
        currentPanelInitialise(currentPanel);
    }

    //unlike the welcomeActions method which is invoked from inside this class, below method is invoked from inside the GameScreen GUI and is passed in the column
    public void dropToken(int column) {
        JButton[][] gridButtons = currentPanel.getGridButtons();
        int row = gridButtons.length;

        //check if the board is full before dropping
        //function is whether the JButton is of the default colour
        Predicate<JButton> isDefaultColour = button -> button.getBackground() == currentPanel.getDefaultColour();
        //pass all the buttons and the above function and return true if no button is the default colour
        boolean boardFull = gameBoard.isBoardFull(gridButtons, isDefaultColour);

        //if above true, showFrame the GUI for where the board is full and execute its logic
        if(boardFull){
            showBoardFull();
        } else {

            //define a lambda to handle token placement for each player to be invoked
            BiConsumer<Integer, Color> placeToken = (col, color) -> {
                //checks if the column is full first
                if (gridButtons[0][col].getBackground() == currentPanel.getDefaultColour()) {
                    //loop backwards from the top row and place colour token at lowest cordinate
                    for (int r = row - 1; r >= 0; r--) {
                        //
                        if (gridButtons[r][col].getBackground() == currentPanel.getDefaultColour()) {
                            gridButtons[r][col].setBackground(color);
                            //invoke method to switch player
                            switchTurns();
                            break;
                        }
                    }

                    //if the column is full and it was a human player that tried to place the token, invoke method to showFrame screen
                } else if(!(currentPlayer instanceof Player.AI)){
                    showColumnFull();
                }
            };

            //human move calls the lambda and passes the paramater column obtained from the action listener and colour as arguments
            if (!(currentPlayer instanceof Player.AI)) {
                placeToken.accept(column, currentPlayer.getColour());
            }

            //call the makeAIMove method from the subclass and return the value to an int effictive final to use by the lambda
            if (currentPlayer instanceof Player.AI) {
                int columnAI = ((Player.AI) currentPlayer).makeAIMove(gridButtons[0].length);
                //lambda to now place this token based on the column
                placeToken.accept(columnAI, currentPlayer.getColour());
            }

            //check game condition and handle game exit
            isGameOver();
        }
    }

    //simple msg dialgoue to inform user
    private void showColumnFull(){
        GUI fullPanel = new ColumnFull();
        fullPanel.showScreen();
    }

    //showFrame gui for full board and restart the game from the welcome screen
    private void showBoardFull(){
        removeFrame();
        GUI fullPanel = new ColumnFull.BoardFull();
        fullPanel.showScreen();
        start();
    }

    private void showWinner(String winner){
        removeFrame();
        currentPanel = new WinnerScreen(winner);
        currentPanelInitialise(currentPanel);

        //restart the whole process from welcome screen
        start();
    }

    //was having a issue with multiple frames so forced dispose of the frame
    //instantiating a gui always create a new frame
    private void removeFrame(){
        JFrame frame = currentPanel.getFrame();
        frame.dispose();
    }

    //change the current player, dynamic enough for future if we extended the number of players eventually
    public void switchTurns() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
    }

    //broke up win condition into inner class.
    public void isGameOver(){
        WinCondition winnerCheck = new WinCondition();

        //pass simple predicate of player argument and then test argument for check if that player is a winner
        winnerCheck.gameOverPlayersCheck(
                p -> winnerCheck.checkWinner(p.getColour())
        );
    }

    //inner class groups win logic and accesses parent game controller fields
    private class WinCondition {

        //This function is a loop predicate of Player class type and the then inputted argument
        public void gameOverPlayersCheck(
                Predicate<Player> playerPredicate) {
            //loop the players array for each player
            for (Player p : players) {
                //test the player for the test argument checkWeinner
                if (playerPredicate.test(p)) {
                    //if true invoke the winner gui
                    String message = "Winner is " + p.toString();
                    showWinner(message);
                    return;
                }
            }
        }

        //this is complicated and invokes another method so going to repeat code on purpose
        //not going to use the board loop function
        public boolean checkWinner(Color playerColor) {
            JButton[][] gridButtons = currentPanel.getGridButtons();
            int maxRows = gridButtons.length;
            int maxColumns = gridButtons[0].length;

            // create an array of transformation vectors for the loop to check once it finds a player colour: right, down, down-right, and down-left
            int[][] directions = {
                    {0, 1},   // Right
                    {1, 0},   // Down
                    {1, 1},   // Down-right
                    {1, -1}   // Down-left
            };

            // loop through all grid positions
            for (int row = 0; row < maxRows; row++) {
                for (int col = 0; col < maxColumns; col++) {
                    // check if the current button matches player's color
                    if (gridButtons[row][col].getBackground() == playerColor) {
                        // now colour found, loop through the vector array
                        for (int[] direction : directions) {
                            //for each vector type (right, down, diagnols etc), apply the transformation to search
                            if (checkDirection(row, col, direction[0], direction[1], playerColor, gridButtons, maxRows, maxColumns)) {
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        }

        //apply the vector type given int he argument in a new 4 move loop
        private boolean checkDirection(int row, int col, int rowDelta, int colDelta, Color playerColor, JButton[][] gridButtons, int maxRows, int maxColumns) {

            // loop in the modified direction and chek if there's 4 consecutive tokens in the irection
            for (int i = 1; i < 4; i++) {

                //to go right (vector 0), row = 1, rowdelta 0 * i = always 1
                int newRow = row + rowDelta * i;
                //going right, col = 1 + colDelta = 1, * i, will move column right by one each iteration of i
                int newCol = col + colDelta * i;

                // as vector modify range +-, make sure we donb't exceed board range and return false if we do
                if (newRow < 0 || newRow >= maxRows || newCol < 0 || newCol >= maxColumns) {
                    return false;
                }

                // Check if the token at the new position matches the player's color
                if (gridButtons[newRow][newCol].getBackground() != playerColor) {
                    return false;
                }
            }
            return true;
        }

    }
}
