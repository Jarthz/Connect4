//parent abstract class of all GUI subclasses. Has a single abstract method all must override
//owns the JFrame creation which all subclasses inherit

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class GUI {


//protected class variables to be accessible by subclasses of GUI

    //static JFrame so only one for all
    protected static JFrame frame;
    protected JButton[][] gridButtons;
    protected GameController controller;
    protected ArrayList<JButton> playerButtons;
    protected JButton startButton;
    //decided to make colour non static for the class. All subclass instances adhere for consistency
    protected static Color defaultColour = Color.WHITE;
    //can add to this public colour scheme in the future
    //this was originally a Color[]
    protected static ArrayList<Color> colours = new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW));

    public GUI() {
        frame = new JFrame("Connect4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
    }

    //invoked externally to force each subclass to a specific controller object
    public void setController(GameController controller) {
       this.controller = controller;
    }

    //this needs to be overridden by all subclasses.
    protected abstract void showScreen();

    //utility for the controller
    public void showFrame(){
        frame.setVisible(true);
    }

    //standard getters and setters

    //getter to get the grid buttons for the controller to use
    public JButton[][] getGridButtons(){
        return gridButtons;
    }

    //i changed the color[] to be an arraylist object instead of an array
    //to accomdate the classes that inherit this change so they don't have to refactor their code for this update
    //i modify the arrylist to being an array in the getter to return the expect type
    public Color[] getColours(){
        Color[] coloursArray = colours.toArray(new Color[colours.size()]);
        return coloursArray;
    }

    public ArrayList<Color> getColoursArray(){
        return colours;
    }

    public Color getDefaultColour(){
        return defaultColour;
    }

    public void setDefaultColour(Color colour){
        this.defaultColour = colour;
        updateColourScheme();
    }

    //future state to update the gamescreen background
    protected void updateColourScheme(){
        this.frame.repaint();
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

    public JButton[] getPlayerButtons(){
        return playerButtons.toArray(new JButton[playerButtons.size()]);
    }

    public JFrame getFrame(){
        return frame;
    }

    public void setButtonListeners(ActionListener listener){
    }

    public void setPlayerButtons(JButton button){
        playerButtons.add(button);
    }
}


