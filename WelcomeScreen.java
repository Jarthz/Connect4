//subclass of our GUI parent to showFrame the Welcome Screen an initalise players

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WelcomeScreen extends GUI {
    private JPanel welcomePanel;

    public WelcomeScreen() {
        super();
        playerButtons = new ArrayList<>();
    }

    //mandatory override, public so accessable to the game controller
    @Override
    public void showScreen() {
        welcomePanel = new JPanel();
        //static layout for now
        welcomePanel.setLayout(new GridLayout(3, 1));

        //two choices of player buttons (PVE or PVP)
        JButton onePlayerButton = new JButton("1 Player");
        JButton twoPlayerButton = new JButton("2 Players");

        JButton startButton = new JButton("Start Game");

        //add all the buttons to our array of buttons
        setPlayerButtons(onePlayerButton);
        setPlayerButtons(twoPlayerButton);


        setStartButton(startButton);

        //this was for fun
        onePlayerButton.setToolTipText("Local game against an AI");
        twoPlayerButton.setToolTipText("Local game PVP");

        //we'll callback to the GameController the action listener to properly segregate duties
        //gamecontroller will assign the logic rather than do it here in the gui
        onePlayerButton.setActionCommand("onePlayer");
        twoPlayerButton.setActionCommand("twoPlayers");
        startButton.setActionCommand("start");

        //defensively grey startbutton so cannot start game without selecting players
        disableButton(startButton);

        //add buttons to panel, panel to frame, showFrame the frame
        welcomePanel.add(onePlayerButton);
        welcomePanel.add(twoPlayerButton);
        welcomePanel.add(startButton);

        frame.add(welcomePanel);

        showFrame();
    }

    //override the placeholder in the parent class
    //loop through all the buttons in the panel and add on an action listener object from the method argument
    //this sets up the gameController to listen to the button clicks so that it can define actions rather than in here
    public void setButtonListeners(ActionListener listener){
        for(Component component : welcomePanel.getComponents()){
            if(component instanceof JButton){
                ((JButton) component).addActionListener(listener);
            }
        }
    }
}