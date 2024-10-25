import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class GUI {


//protected class variables to be accessible by subclasses of GUI

    protected JFrame frame;
    protected JButton[][] gridButtons;
    protected GameController controller;
    protected ArrayList<JButton> playerButtons;
    protected JButton startButton;


    //decided to make colour non static for the class. This allows us to modify instances to have seperate default scheme
    //hallowwen spooky

    protected Color defaultColour = Color.WHITE;


    //can add to this public colour scheme in the future
    //this was originally and array, discussion in getter method
    protected ArrayList<Color> colours = new ArrayList<>(Arrays.asList(Color.RED, Color.YELLOW));

    public GUI() {
        frame = new JFrame("Connect4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

    }

    //object specific method for each subclass object
    public void setController(GameController controller) {
       this.controller = controller;
    }

    //this needs to be overridden by all subclasses. Any subclass GUI object needs to have a method
    //that makes it visible to the user
    protected abstract void showScreen();

    //utility for the controller
    public void show(){
        frame.setVisible(true);
    }

    //getter to get the grid buttons for the controller to use
    public JButton[][] getGridButtons(){
        return gridButtons;
    }


    //here is the painful part of generalisation. This getter was originally Color[] return type
    //i changed the color[] to be an arraylist object instead of an array
    //to accomdate the classes that inherit this change so they don't have to refactor their code for this update
    //i modify the arrylist to being an array in the getter to return the expect type
    public Color[] getColours(){
        Color[] coloursArray = colours.toArray(new Color[colours.size()]);
        return coloursArray;
    }

    //new method
    //i tried overloading but learnt that this doesn't work because the return types are different and that's the whole point
    //so now it's a new method for the arraylist
    public ArrayList<Color> getColoursArrayList(){
        return colours;
    }

    //this is new so that now we could add to our colour arraylist in the future
    public void addColour(Color colour){
        colours.add(colour);
    }

    public void removeColour(Color colour){
        colours.remove(colour);
    }

    public void removeAllColours(){
        colours.clear();
    }

    public Color getDefaultColour(){
        return defaultColour;
    }

    public void setDefaultColour(Color colour){
        this.defaultColour = colour;
        updateColourScheme();

    }

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


