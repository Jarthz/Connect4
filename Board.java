//Create an instance of a gameboard object

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class Board {

    private int row;
    private int column;

    public Board(int width, int height) {
        this.row = width;
        this.column = height;
    }

    //created a functional reusable method so that it could be generalised for other board loop use cases
    //loop through board and take as arguments x,y cordinates and have no required return, so void type where we do something on those coordinates
    public void loopBoard(BiConsumer<Integer, Integer> action) {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                action.accept(i,j);
            }
        }
    }

    //method to take gridbuttons as arguments so we can assess their state based on the predicate argument
    //this method is invoked outside of the class and fed the argument by the game controller
    public boolean isBoardFull(JButton[][] gridButtons, Predicate<JButton> predicate) {
        //learnt below whereby cannot use a primitive boolean type as it doesn't meet the lambda requirement of being effecitvely final
        AtomicBoolean isFull = new AtomicBoolean(true);

        //reuse the loopboard method above so not to repeat code loops, passing x,y coordinates and then as an action, assess the boolean argumetn in this method at those coorindates in the loop
        loopBoard((x, y) -> {

            //in the one implimentation, check that x/y button == default colour
            if(predicate.test(gridButtons[x][y])){
                isFull.set(false);
            }
        });
        return isFull.get();
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }
}
