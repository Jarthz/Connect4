//top level class, not nested so non stactic so can use the same public fields as abstract g

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class WelcomeScreen extends GUI {
    private JPanel mainPanel;



    public WelcomeScreen() {
        super();
        playerButtons = new ArrayList<>();
    }

    @Override
    public void showScreen() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 1));

        JButton onePlayerButton = new JButton("1 Player");
        JButton twoPlayerButton = new JButton("2 Players");
        JButton startButton = new JButton("Start Game");

        setPlayerButtons(onePlayerButton);
        setPlayerButtons(twoPlayerButton);
        setStartButton(startButton);


        onePlayerButton.setToolTipText("Local game against an AI");
        twoPlayerButton.setToolTipText("Local game PVP");

        onePlayerButton.setActionCommand("onePlayer");
        twoPlayerButton.setActionCommand("twoPlayers");
        startButton.setActionCommand("start");
        //parent method: by default we'll grey this out
        disableButton(startButton);


        mainPanel.add(onePlayerButton);
        mainPanel.add(twoPlayerButton);
        mainPanel.add(startButton);

        frame.add(mainPanel);
        show();
    }

    //the game controller will cast the WelcomeScreen class type
    //so that it gets access to this method
    public void setButtonListeners(ActionListener listener){
        for(Component component : mainPanel.getComponents()){
            if(component instanceof JButton){
                ((JButton) component).addActionListener(listener);
            }
        }
    }


    //these we'll override the GUI parent class and be accessed through that
    public void setPlayerButtons(JButton button){
        playerButtons.add(button);
    }

    public JButton setStartButton(JButton button){
        return startButton = button;
    }

    public JButton getStartButton(){
        return startButton;
    }

    public JButton[] getPlayerButtons(){
        return this.playerButtons.toArray(new JButton[playerButtons.size()]);
    }



}