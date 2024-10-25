import java.awt.*;

public class Main {

    public static void main(String[] args){

        Board board = new Board(7,6);

        GameController game = new GameController(board);

        game.start();

        Board halloweenBoard = new Board(7,6);
        GameController halloweenGame = new GameController(halloweenBoard);


        //this creates a second concurrent instance when the first game you play is in spooky colours
        //i've kept it because it was helpful for me to realise that i haven't done loose coupling in the game controller
        //what i mean is that i chained all the screen methods together so that if i wanted to inject a black colour scheme in the middle method, i have to carry that parameter from the first method in the chain.
        halloweenGame.start(Color.BLACK);
        halloweenGame.removeColourScheme();
        halloweenGame.addColourScheme(Color.ORANGE);
        halloweenGame.addColourScheme(Color.YELLOW);




    }
}
