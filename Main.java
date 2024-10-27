public class Main {

    public static void main(String[] args){

        Board board = new Board(7,6);

        GameController game = new GameController(board);

        game.start();
    }
}
