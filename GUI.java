import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public abstract class GUI {


//protected class variables to be accessible by subclasses of GUI

    protected JFrame frame;
    protected JButton[][] gridButtons;
    protected GameController controller;
    protected ArrayList<JButton> playerButtons;
    protected JButton startButton;




        //can add to this public colour scheme
    public Color[] colours = {Color.RED, Color.YELLOW,};

    public GUI() {
        frame = new JFrame("Connect4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

    }

    //object specific method for each subclass object
    public void setController(GameController controller) {
       this.controller = controller;
    }

    //this needs to be overridden by all subclasses
    public abstract void showScreen();

    public void show(){
        frame.setVisible(true);
    }

    //getter to get the grid buttons for the controller to use
    public JButton[][] getGridButtons(){
        return this.gridButtons;
    }

    public Color[] getColours(){
        return colours;
    }

    public void disableButton(JButton... buttons){
        for(JButton button : buttons){
            button.setEnabled(false);
        }
    }

    public void enableButton(JButton... buttons){
        for(JButton button : buttons){
            button.setEnabled(true);
        }
    }


    public JButton setStartButton(JButton button){
        return startButton = button;
    }

    public JButton getStartButton(){
        return startButton;
    }

    public void setPlayerButtons(JButton button){
        playerButtons.add(button);
    }

    public JButton[] getPlayerButtons(){
        return playerButtons.toArray(new JButton[playerButtons.size()]);
    }

    public JFrame getFrame(){
        return frame;
    }




}


