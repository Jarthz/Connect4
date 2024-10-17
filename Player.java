import java.awt.Color;

public class Player {


    public Color colour;

    public Player(Color colour) {
        this.colour = colour;
    }


    public static class AI extends Player {

        public AI(Color colour) {
            super(colour);
            this.colour = colour;

        }

        public int makeAIMove(int columns) {
            return (int) (Math.random() * columns);

        }

    }


}
