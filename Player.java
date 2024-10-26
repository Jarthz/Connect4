//class to create player instances. Uses method rule set from playerInterface

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Player implements PlayerInterface{
    private Color colour;
    private String name;
    private int Score;

    public Player(Color colour, String name) {
        this.colour = colour;
        this.name = name;

    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Color getColour() {
        return colour;
    }

    //didn't build score retention as out of scope but have to adhere to my interface and contain these blank methods
    @Override
    public int getScore() {
        return Score;
    }

    @Override
    public void incrementScore(){
        Score++;
    }

    @Override
    public String toString(){
        return name;
    }

    //public class level method that allows outside class to create instances of the player class
    public static List<Player> setupPlayers(int numberOfPlayers, boolean hasAI, Color[] colours){
        //error check so that you can't create more players than assigned colours
        if(numberOfPlayers > colours.length){
            throw new IllegalArgumentException("Number of players must be less than or equal to number of colours");
        }

        //List generic type initlising ArrayList to hold the player or colour objects
        List<Player> players = new ArrayList<>();
        List<Color> availableColours = new ArrayList<>(List.of(colours));

        //loop for the number of player parameter of the method and creates
        for(int i = 1; i <= numberOfPlayers; i++) {
            Player player = new Player(availableColours.get(i-1), "Player " + i);
            players.add(player);
        }

        //if boolean true then create AI object (subclass) of the Player type
        if (hasAI) {
            Player aiPlayer = new AI( availableColours.getLast(),"AI");
            players.add(aiPlayer);
        }

        return players;
    }

//static inner/nested class AI. Inherits the structure of player without needing to be an instance of a player object
    //in theory we could setup AI vs AI in the future by doing this. Bit boring but that's the decision process
    //if this gets more complex, then would actually make this a standalone subclass but for now this is fine

    public static class AI extends Player {

        public AI(Color colour, String name) {
            super(colour, name);
        }

        //this could be enhanced but sufficient for now
        //randomly assign a column based on the available columns
        //invoked by the game controller when dropping tokens
        public int makeAIMove(int columns) {
            return (int) (Math.random() * columns);
        }

    }


}
