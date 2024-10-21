import javax.swing.*;

public class ColumnFull extends GUI{

    public ColumnFull(){
        super();
    }

    public void showScreen(){
        JOptionPane.showMessageDialog(frame, "Column Full", "Column Full", JOptionPane.INFORMATION_MESSAGE);
    }

    //nested inner class
    public static class BoardFull extends ColumnFull{
        public BoardFull(){
            super();
        }

        public void showScreen(){
            JOptionPane.showMessageDialog(frame, "Board Full", "Board Full", JOptionPane.INFORMATION_MESSAGE);
        }

    }
}
