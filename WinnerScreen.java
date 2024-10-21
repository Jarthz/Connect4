import javax.swing.*;

public class WinnerScreen extends GUI {
    private String winner;

    public WinnerScreen(String winner) {
        super();
        this.winner = winner;
    }

    @Override
    public void showScreen() {

        JOptionPane.showMessageDialog(frame, winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        frame.setVisible(false);
        frame.dispose();
    }

}
