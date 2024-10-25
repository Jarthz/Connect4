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


    //didn't build this in the end but have to adhere to my interface and contain this blank methods
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

    //public class level method to setup objects of this class from the controller
    public static List<Player> setupPlayers(int numberOfPlayers, boolean hasAI, Color[] colours){
        //error handling
        if(numberOfPlayers > colours.length){
            throw new IllegalArgumentException("Number of players must be less than or equal to number of colours");
        }

        List<Player> players = new ArrayList<>();
        List<Color> availableColours = new ArrayList<>(List.of(colours));


        for(int i = 1; i <= numberOfPlayers; i++) {
            Player player = new Player(availableColours.get(i-1), "Player " + i);
            players.add(player);
        }

        if (hasAI) {
            Player aiPlayer = new AI( availableColours.getLast(),"AI");
            players.add(aiPlayer);
        }

        return players;
    }

//static inner/nested class AI. Inherits the structure of player without needing to be an instance of player
    //in theory we could setup AI vs AI in the future by doing this. Bit boring but that's the decision process
    //if this gets more complex, then would actually make this a standalone subclass but for now this is fine

    public static class AI extends Player {

        public AI(Color colour, String name) {
            super(colour, name);
        }

        //to do, review this so that the AI doesn't try and drop to a full column
        public int makeAIMove(int columns) {
            return (int) (Math.random() * columns);

        }

      //  @Override
        //public String toString(){
          //  return super.toString();
       // }

    }


}
