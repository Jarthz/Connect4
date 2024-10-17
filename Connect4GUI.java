import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Connect4GUI {

    private JFrame frame;
    private JPanel gamePanel;
    private JPanel mainPanel;
    private JButton[][] gridButtons;
    private int column = 6;
    private int row = 7;
    private GameController controller;

    private Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA};

    public Connect4GUI() {
        frame = new JFrame("Connect4");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));

        JButton onePlayerButton = new JButton("1 Player");
        JButton twoPlayerButton = new JButton("2 Players");
        JButton startButton = new JButton("Start Game");

        onePlayerButton.addActionListener(e -> setupPlayers(1, true));
        twoPlayerButton.addActionListener(e -> setupPlayers(2, false));
        startButton.addActionListener(e -> startGame());

        mainPanel.add(onePlayerButton);
        mainPanel.add(twoPlayerButton);
        mainPanel.add(startButton);

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private void startGame(){
        frame.remove(mainPanel);
        gamePanel = createGamePanel();
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
    }

    private void setupPlayers(int numberOfPlayers, boolean AI){
        List<Player> playerList = new ArrayList<>();


        for(int i = 0; i < numberOfPlayers; i++){
            Player player = new Player
        }

    }


    private JPanel createGamePanel(){
        //create the board using the grid class layout manager
        gamePanel = new JPanel(new GridLayout(row, column));

        //initialise an array of jbutton objects
        gridButtons = new JButton[row][column];

        for(int i = 0; i < row; i++) {
            for(int j = 0; j < column; j++) {
                //create a button
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                //add the button to the array for reference
                gridButtons[i][j] = button;
                //need an unmodified variable (make effectively final) for lambdas
                int finalCol = j;


                /*
                I've put in an overly robust explanation for the below use of functional interface, abstract class inheretence and lambdas combination
                This is mainly to highlight my understanding of the logic given we haven't covered it

                Objective: create an action listener for each button and add it to the button and then do something

                JButton class extends the AbstractButton class so it inherits the method addActionListerner
                We invoke the addActionListenr method which in turn impliments the actionListener interface to the button

                actionListener is a functional interface as only has the one abstract method 'actionPerformed'
                Wehn the JButton button object implements the Listener interface, it gets Class ActionEvent related to it as an instanced object


                actionPerformed method needs the parameter e of the class ActionEvent
                'e' is a high level event and here, because the button object added the method, clicking the button generates 'e' and invokes the method actionPerformed

                becasue actionPerformed is a method in an interface we have to impliment it
                actionPerformed is the only abstract method of the interface
                we don't need to use the name of the method to impliment it


                We could create a method actionPerformed(ActionEvent e) and then add actions in the body of this new method

                or we use a lambdas expression that takes actionEvent's e from the button object as the parameter, and pass this directly to the method when we add the ActionListner interface
                This will invoke actionPerformed method for us without needing to write an explicit method

                finally we'll add an expression body to the lambdas that will call another method dropToken and the parameter which column to actually do something once it's confirmed and event has occured

                 */

                button.addActionListener((ActionEvent e) -> {
                    controller.dropToken(finalCol);
                });
                //add the button to the gui
                gamePanel.add(button);

            }
        }

        //TO do put in instructions




        frame.add(gamePanel);
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public void show(){
        frame.setVisible(true);
    }

    //getter to get the grid buttons for the controller to use
    public JButton[][] getGridButtons() {
        return gridButtons;
    }


//create win logic





}