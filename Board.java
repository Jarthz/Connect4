import javax.swing.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class Board {

    private int row;
    private int column;

    public Board(int width, int height) {
        this.row = width;
        this.column = height;

    }

    //modified the original loop to consume a fuction as a parameter to imnpliment a GUI instead of printing to console
    public static void buildBoard(int width, int height, BiConsumer<Integer, Integer> action) {
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                action.accept(i,j); //do the function passed in
            }
        }
    }

    public boolean isBoardFull(JButton[][] gridButtons, Predicate<JButton> predicate) {
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                if(predicate.test(gridButtons[i][j])){
                    return false;
                }
            }
        }
        return true;
    }

    public int getRow(){
        return row;
    }

    public int getColumn(){
        return column;
    }



}
