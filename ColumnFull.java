//simple class and nested inner class that handle user prompts when they try and break the game
import javax.swing.*;

public class ColumnFull extends GUI{

    public ColumnFull(){
        super();
    }

    @Override
    public void showScreen(){
        JOptionPane.showMessageDialog(frame, "Column Full", "Column Full", JOptionPane.INFORMATION_MESSAGE);
    }

    //nested inner class
    public static class BoardFull extends ColumnFull{
        public BoardFull(){
            super();
        }

        @Override
        public void showScreen(){
            JOptionPane.showMessageDialog(frame, "Board Full", "Board Full", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
