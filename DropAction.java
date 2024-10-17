import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DropAction implements ActionListener {
    private int column;

    public DropAction(int column) {
        this.column = column;
    }

    public void actionPerformed(ActionEvent e) {
        columnDrop(column);
    }

    private void columnDrop(int column) {
        System.out.println("Clicked on column " + column);
    }

}
