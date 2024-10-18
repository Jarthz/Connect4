import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {


    public Color colour;
    public String name;

    public Player(Color colour, String name) {
        this.colour = colour;
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

    public static List<Player> setupPlayers(int numberOfPlayers, boolean hasAI, Color[] colours){
        List<Player> players = new ArrayList<>();
        List<Color> availableColours = new ArrayList<>(List.of(colours));

        Collections.shuffle(availableColours);

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


    public static class AI extends Player {

        public AI(Color colour, String name) {
            super(colour, name);
            this.colour = colour;
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
