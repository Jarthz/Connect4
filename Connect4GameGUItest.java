import javax.swing.*;
import java.awt.*;

public class Connect4GameGUItest {

    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] gridButtons;

    public Connect4GameGUItest() {
        frame = new JFrame("Connect 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Board layout (6 rows x 7 columns)
        boardPanel = new JPanel(new GridLayout(6, 7));
        gridButtons = new JButton[6][7];

        // Initialize grid buttons
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(100, 100));
                gridButtons[row][col] = button;
                boardPanel.add(button);
            }
        }

        // Add board to frame
        frame.add(boardPanel, BorderLayout.CENTER);

        // Create a status panel at the bottom
        JPanel statusPanel = new JPanel();
        JLabel statusLabel = new JLabel("Player 1's turn");
        statusPanel.add(statusLabel);

        frame.add(statusPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Connect4GameGUItest();
    }
}
