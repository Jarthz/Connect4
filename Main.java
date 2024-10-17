public class Main {

    public static void main(String[] args){

        Connect4GUI gui = new Connect4GUI();

        GameController game = new GameController(gui);

        gui.setController(game);

        gui.show();

    }
}
