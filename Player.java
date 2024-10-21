import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Player implements PlayerInterface{


    private Color colour;
    private String name;
    private int wins;
    //consider adding a win count

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


    @Override
    public int getScore() {
        return wins;
    }

    @Override
    public void incrementScore(){
        wins++;
    }


    @Override
    public String toString(){
        return name;
    }

    //public class level method to setup objects of this class from the controller
    public static List<Player> setupPlayers(int numberOfPlayers, boolean hasAI, Color[] colours){
        List<Player> players = new ArrayList<>();
        List<Color> availableColours = new ArrayList<>(List.of(colours));


        for(int i = 1; i <= numberOfPlayers; i++) {
            Player player = new Player(availableColours.get(i-1), "Player " + i);
            players.add(player);
        }

        if (hasAI) {
            Player aiPlayer = new AI( Color.YELLOW,"AI");
            players.add(aiPlayer);
        }

        return players;
    }

//static nested class AI. Inherits the structure of player without needing to be an instance of player
    //in theory we could setup AI vs AI in the future. Bit boring though...

    public static class AI extends Player {

        public AI(Color colour, String name) {
            super(colour, name);
        }

        public int makeAIMove(int columns) {
            return (int) (Math.random() * columns);

        }

        @Override
        public String toString(){
            return super.toString();
        }

    }


}
