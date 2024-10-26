//main game display of coloured tokens in a grid

import javax.swing.*;
import java.awt.*;

public class GameScreen extends GUI {

    private JPanel gamePanel;
    private JButton[][] gridButtons;
    private final Board board;

    public GameScreen(Board board) {
        super();
        this.board = board;
    }

    //override the abstract method in the parent abstract class so can be invoked by the game controller
    @Override
    public void showScreen(){
        //seperated the create panel as unique method
        gamePanel = createGamePanel();

        //add to and show Frame
        frame.add(gamePanel);
        showFrame();

    }

    //this is a different take than the WelcomeScreen. The WelcomeScreen is better OOP as handles exclusively setting up the buttons and not the actionPerformed logic
    //here we nest lambda when adding the action listener
    private JPanel createGamePanel(){

        //main game panel based on the sizes given from the board object created in main
        gamePanel = new JPanel(new GridLayout(board.getRow(), board.getColumn()));

        //initialise an array of Jubbton objects. same dimensions as the board and panel
        gridButtons = new JButton[board.getRow()][board.getColumn()];

        //invoke the board loop method and pass several actions
        board.loopBoard((x, y) ->{
            //for each coorindate create a button
            JButton button = new JButton();
            //make it default colour
            button.setBackground(defaultColour);
            //add the button and its coordinate to the array of buttons
            gridButtons[x][y] = button;

            /*
            for a robust explanation here refer to subheading 'Interface' in the submitted report
            Below is BAD encapsulation. As comparison, the welcomeScreen Class GUI does not handle action events directly, rather being invoked byt he game controller to seperate concerns
            this was for my benefit in learning but it is more concise, otherwise i would have to write more verbose methods in the game controller
            */

            //need an unmodified variable of the column to drop into (make effectively final) for lambda
            int finalCol = y;

            //add the listener and immediately invoke the actionPerformed method and create lambda body to call the drop token method from the GameController
            button.addActionListener(event -> {
                controller.dropToken(finalCol);
            });
            //add the button to the gui
            gamePanel.add(button);
        });
        return gamePanel;

    }

//this method is accessed outside of the GUI heirachy and we want to return the grid buttons here rather than the parent
@Override
    public JButton[][] getGridButtons(){
        return this.gridButtons;
    }

    public void setDefaultColour(Color colour) {
        super.setDefaultColour(colour);
    }


}


