import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameScreen extends GUI {

    private JPanel backPanel;
    private JPanel gamePanel;
    private JButton[][] gridButtons;
    private Board board;



    public GameScreen(Board board) {
        super();
        this.board = board;
    }

    //override the abstract method in the parent abstract class
    @Override
    public void showScreen(){
        gamePanel = createGamePanel();
        frame.add(gamePanel);
        show();

    }

    private JPanel createGamePanel(){
        //create a back panel so that we can print stuff in addition to the game board
        //dont' actually do this currently
        backPanel = new JPanel(new BorderLayout());

        //main game panel based on the sizes given from the board object created in main
        gamePanel = new JPanel(new GridLayout(board.getRow(), board.getColumn()));

        //initialise an array of Jubbton objects. same dimensions as the board and panel
        gridButtons = new JButton[board.getRow()][board.getColumn()];


        board.buildBoard(board.getRow(), board.getColumn(), (x, y) ->{
            JButton button = new JButton();
            button.setBackground(defaultColour);
            //add the button to the array for reference
            gridButtons[x][y] = button;
            //need an unmodified variable (make effectively final) for lambdas
            int finalCol = y;

            /*
        I've put in an overly robust explanation for the below use of functional interface, abstract class inheretence and lambdas combination
        This is mainly to highlight my understanding of the logic given we haven't covered it

        Objective: create an action listener for each button and add it to the button and then do something

        JButton class extends the AbstractButton class so it inherits the method addActionListerner
        We invoke the addActionListenr method which in turn impliments the actionListener interface to the button

        actionListener is a functional interface as only has the one abstract method 'actionPerformed'
        actionPerformed method needs the parameter e of the class ActionEvent

        Wehn the JButton button object implements the Listener interface, it gets Class ActionEvent related to it as an instanced object

        'e' is a high level event and because the button object added the method, clicking the button generates 'e'

        becasue actionPerformed is a method in an interface we have to impliment it
        BUT actionPerformed is the only abstract method of the interface
        Therefore we don't need to use the name of the method to impliment it directly

        We could create a direct method "actionPerformed(ActionEvent e)" and then add actions in the body of this new method
        (which we do for the weclome screen)

        or we use a lambdas expression that takes actionEvent's e from the button object as the parameter, and pass this parameter directly to the addActionListner method which will invoke the abstract method actionPerformed as it is of the correct parameter type
        This will invoke actionPerformed method for us without needing to write an explicit method

        finally we'll add an expression body to the lambdas that will call another method dropToken and the parameter which column to actually do something once it's confirmed and event has occured
        this expresison will be invoked when the method actionPerformed is returned true

        this is BAD encapsulation. Notice the welcomeScreen Class GUI does not handle action events directly, rather being invoked byt he game controller to seperate concerns
        I just thought this would be cool nesting a lambdas in a lambdas and something to discuss

         */

            button.addActionListener((ActionEvent e) -> {
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


