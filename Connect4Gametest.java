import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Connect4Gametest {

    private JButton[][] gridButtons = new JButton[6][7]; // 6 rows, 7 columns

    public Connect4Gametest() {
        JFrame frame = new JFrame("Connect 4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 7)); // 6 rows and 7 columns

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                JButton button = new JButton("");
                gridButtons[row][col] = button;
                int finalCol = col; // Lambdas require the variable to be effectively final
                button.addActionListener((ActionEvent e) -> {
                    handleButtonClick(finalCol);
                });
                frame.add(button);
            }
        }

        frame.pack();
        frame.setVisible(true);
    }

    // This method handles the button click when a player drops a disc
    private void handleButtonClick(int column) {
        // Logic to drop a disc in the lowest available row of the specified column
        System.out.println("Column clicked: " + column);
        // Add more game logic here, e.g., updating the grid state and checking for wins
    }

    public static void main(String[] args) {
        new Connect4Gametest();
    }
}
