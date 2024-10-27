//Controller class interacts with player input, GUI and game logic. It handles the execution flow.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class GameController {

    //create a current player so we can generalise methods
    private Player currentPlayer;
    //list of players to loop through
    private List<Player> players;
    //so we wrap around the list
    private int currentPlayerIndex = 0;
    //create a current panel so we can generalise GUI methods
    private GUI currentPanel;
    //use the Board object to define our board and ranges
    private final Board gameBoard;
    private int maxRow;
    private int maxCol;
    //overlay on our board JButtons
    private JButton[][] gridButtons;
    private Player winner;

    public GameController(Board gameBoard) {
        this.gameBoard = gameBoard;
        this.maxRow = gameBoard.getRow();
        this.maxCol = gameBoard.getColumn();
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

        //assign action listeners to the JButtons on the welcome screen using a method reference to this listener
        //welcomeAction method argument listens to the ActionPerformed object
        currentPanel.setButtonListeners(this::welcomeActions);
    }

    //receive button event and then do action based on which button
    //either setup players + AI or start game
    private void welcomeActions(ActionEvent e){
        String command = e.getActionCommand();

        switch (command) {
            case "onePlayer" -> {
                setupPlayers(1, true);
                welcomeDefensiveBlockers();
            }
            case "twoPlayers" -> {
                setupPlayers(2, false);
                welcomeDefensiveBlockers();
            }
            case "start" -> startGame();
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
        //initialise the placeholder so we can start accessing the buttons in the game in other methods in this class
        gridButtons = currentPanel.getGridButtons();
    }

    //unlike the welcomeActions method which is invoked from inside this class, below method is invoked from inside the GameScreen GUI
    public void dropToken(int column) {
        //check if the board is full before dropping
        //function is whether the JButton is of the default colour
        Predicate<JButton> isDefaultColour = button -> button.getBackground() == currentPanel.getDefaultColour();

        //use the gamboard helper loop, passing all the buttons and the above function and return true if no button is the default colour
        boolean boardFull = gameBoard.isBoardFull(gridButtons, isDefaultColour);

        //if above true, showFrame the GUI for where the board is full and execute its logic
        if(boardFull){
            showBoardFull();
        } else {

            //define a lambda to handle token placement for each player
            BiConsumer<Integer, Color> placeToken = (col, color) -> {
                //checks if the column is full first
                if (gridButtons[0][col].getBackground() == currentPanel.getDefaultColour()) {
                    //loop backwards from the last row and place colour token first white
                    //a little confusing because the last row in the array is technically the bottom row, so start at the bottom and work up until you find white
                    for (int r = maxRow - 1; r >= 0; r--) {
                        if (gridButtons[r][col].getBackground() == currentPanel.getDefaultColour()) {
                            gridButtons[r][col].setBackground(color);
                            //after placing, change the current player
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
            checkGameOver();
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

    //simple dialogue box to show game over, confirm winner and start game again
    private void showWinner(String winner){
        removeFrame();
        currentPanel = new WinnerScreen(winner);
        currentPanelInitialise(currentPanel);

        //restart the whole process from welcome screen
        start();
    }

    //was having a issue with multiple frames so forced dispose of the frame
    //instantiating a gui subclass always create a new frame
    private void removeFrame(){
        JFrame frame = currentPanel.getFrame();
        frame.dispose();
    }

    //change the current player, dynamic enough for future if we extended the number of players
    public void switchTurns() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
    }

    //loop through the players and check whose won, if thye have, end game
    public void checkGameOver(){
        //use the inner Class game over logic
        WinCondition winnerCheck = new WinCondition();

        //pass player as argument to a loop for players and return winning player to variable
        winner = winnerCheck.playersCheck(
                p -> winnerCheck.checkWinner(p.getColour())
        );

        //if a player was returned from above, somoene won and we endgame
        if(winner != null){
            String message = "Winner is " + winner;
            showWinner(message);
            //safely reset winner. Not necessary as handled in the drop token but worth doing
            winner = null;
        }
    }

    //inner class groups the win logic
    private class WinCondition {

        //This predicate function is a loop of players class type and then the inputted argument from lambda
        //returns a winner name
        public Player playersCheck(
                Predicate<Player> playerPredicate) {
            //loop the players array for each player
            for (Player p : players) {
                //test the player for the test argument and return a player if true
                if (playerPredicate.test(p)) {
                    return p;
                }
            }
            return null;
        }

        //loop to find a player's colour
        //use transformation vector to loop from player's colour 3 times to check winner
        //loop through each transformation vector
        public boolean checkWinner(Color playerColour) {
            //using lambda so non primative
            AtomicBoolean winnerFound = new AtomicBoolean(false);

            // create an array of transformation vectors for the loop to check once it finds a player colour: right, down, down-right, and down-left
            int[][] directions = {
                    {0, 1},   // Right
                    {1, 0},   // Down
                    {1, 1},   // Down-right
                    {1, -1}   // Down-left
            };

             // check if the current button matches player's color using the gamboard helper method
            gameBoard.loopBoard((x, y) -> {
                //find the player's colour
                    if (gridButtons[x][y].getBackground() == playerColour) {
                        // for each vector type
                        for (int[] direction : directions) {
                            //call a helper method to check the vectors for more player colours
                            if (checkDirection(x, y, direction[0], direction[1], playerColour)) {
                                winnerFound.set(true);
                                break;
                            }
                        }
                    }
            });
            return winnerFound.get();
        }

        //apply the vector type given int he argument in a new 4 move loop
        private boolean checkDirection(int row, int col, int rowDelta, int colDelta, Color playerColour) {

            // loop in the modified direction and chek if there's 4 consecutive tokens in the irection
            for (int i = 1; i < 4; i++) {

                //to go right (vector 0, 1), row = 1, rowdelta = 0, * i = always 1
                int newRow = row + rowDelta * i;
                //going right (vector 0, 1), col = 1 + colDelta = 1, * i, will move column right by one each iteration of i
                int newCol = col + colDelta * i;

                // as vector modify range +-, make sure we donb't exceed board range and return false if we do
                if (newRow < 0 || newRow >= maxRow || newCol < 0 || newCol >= maxCol) {
                    return false;
                }

                // Check if the token at the new position matches the player's color
                if (gridButtons[newRow][newCol].getBackground() != playerColour) {
                    return false;
                }
            }
            return true;
        }
    }
}
