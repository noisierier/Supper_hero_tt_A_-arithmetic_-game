import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
/**
 * The Run class serves as the main class for the application.
 * It contains methods for running the game interface and the game logic,
 * as well as serving as the entry point of the game.
 *
 * @author Lan Haitong
 * @version 1.0
 * @since 2024-05-22
 */
public class Run {
    /**
     * The label on the left side of the interface, indicating whether the answer is correct.
     */
    public JLabel left;
    /**
     * The label on the right side of the interface, indicating the rating.
     */
    public JLabel right;
    /**
     * The button to start the game or go to the next question.
     */
    public JButton up;
    /**
     * The main frame containing the game interface.
     */
    public JFrame frame;
    /**
     * The middle panel where the input box and other components are placed.
     */
    public JPanel nor;
    /**
     * Selection box.
     */
    private JComboBox<String> select;
    /**
     * The text field where the user enters their answers.
     */
    public JTextField input;
    /**
     * The label at the top, displaying the current question.
     */
    public JLabel label;
    /**
     * Correct answer
     */
    public static int answer;
    private static int N,X,Counter,initialize,corr;
    /**
     * An array of selectable numbers for the multiplication game.
     */
    String[] ct = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};// List
    /**
     * Main method, program entry
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Run gui = new Run();
        gui.go();
    }

    /**
     * Build the game interface
     */
    public void go() {
        frame = new JFrame("SuperHero Times Tables");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        nor = new JPanel();
        left = new JLabel("Answer");
        right = new JLabel("Rating");
        select = new JComboBox<>(ct);
        select.setPreferredSize(new Dimension(20, select.getPreferredSize().height));
        input = new JTextField(7);
        label = new JLabel("Product:  ");
        up = new JButton("Start");

        // Add listeners
        select.addItemListener(new Sel());
        up.addActionListener(new upbutton());
        input.addKeyListener(new Check());


        ImageIcon image=new ImageIcon("superhero.jpg");
        JLabel label2=new JLabel(image);// Construct a JLabel label with the image
        // Create a scaled image
        Image scaledImage = image.getImage().getScaledInstance(300, 250, Image.SCALE_SMOOTH); // Adjust image size
        label2.setIcon(new ImageIcon(scaledImage));// Set the scaled image as the icon of the JLabel

        // Create a vertical Box to organize components
        Box vBox = Box.createVerticalBox();
        // First add select to the vertical layout
        vBox.add(select);
        vBox.add(Box.createVerticalStrut(10)); // Add some vertical spacing
        // Create a horizontal Box to organize label, input, and up
        Box hBox = Box.createHorizontalBox();
        hBox.add(label);
        hBox.add(input);
        hBox.add(up);
        // Then add the horizontal Box to the vertical layout
        vBox.add(hBox);
        // Add the vertical Box to the nor panel

        nor.add(vBox);
        frame.getContentPane().add(BorderLayout.WEST, left);
        frame.getContentPane().add(BorderLayout.EAST, right);
        frame.getContentPane().add(BorderLayout.CENTER, label2);
        frame.getContentPane().add(BorderLayout.NORTH, nor);
        frame.setSize(500, 400);
        frame.setVisible(true);
    }


    /**
     * Check if the user input answer is correct
     */
    public void checkInput() {
        try {
            String text = input.getText();
            if (!text.isEmpty()) {
                int userValue = Integer.parseInt(text); // Try to convert the text to an integer
                String selectedValue = (String) select.getSelectedItem();
                int expectedValue = answer; // Correct value
                Counter++;// Increase the number of answers
                if (userValue == expectedValue) {
                    // User input is correct
                    left.setText("Correct");
                    corr++;// Increase the number of correct answers
                    Random random = new Random();
                    int n = 1 + random.nextInt(12);
                    N = n;
                    label.setText(""+X+"*"+n+"  ");
                    up.setText("Next");
                    answer = X*n;
                    input.setText("");
                }
                else {
                    // User input is wrong
                    left.setText("Wrong! "+X+" * "+N+" = "+answer);
                    Random random = new Random();
                    int n = 1 + random.nextInt(12);
                    N = n;
                    label.setText(""+X+"*"+n+"  ");
                    up.setText("Next");
                    answer = X*n;
                    input.setText("");
                }
                if(Counter == 5){// Complete 5 games, reset the game
                    right.setText("You got "+corr+" correct!" );
                    up.setText("Start");
                    Counter = 0;// Reset the counter
                    corr = 0;// Reset the number of correct answers
                    initialize = 0;// Reset the flag
                    input.setText("");
                    label.setText("Product  ");
                }
            }
            else {
                // The text box is empty
                JOptionPane.showMessageDialog(frame, "Please enter a number!");
                input.setText("");
            }
        } catch (NumberFormatException nfe) {
            // The user input is not a number
            JOptionPane.showMessageDialog(frame, "Please enter a valid number!");
            input.setText("");
        }
    }
    /**
     * Item listener for the selection box, used to select the multiplication table
     * @see java.awt.event.ItemListener
     */
    public class Sel implements ItemListener{
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                // Code executed when an item in the dropdown box is selected
                String selectedValue = (String) e.getItem();
                int x =Integer.parseInt(selectedValue);
                X = x;
                Random random = new Random();
                int n = 1 + random.nextInt(12);
                N = n;
                if(initialize==1) {
                    label.setText("" + x + "*" + n + "  ");
                }
                answer = x*n;
            }
        }
    }
    /**
     * Key listener for the input box, listens for Enter key events
     */
    public class Check extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if ((e.getKeyCode() == KeyEvent.VK_ENTER)&&(initialize == 1)) {
                checkInput();
            }
            if((e.getKeyCode() == KeyEvent.VK_ENTER)&&(initialize == 0)){
                JOptionPane.showMessageDialog(frame, "Please click the Start button to begin the game!");
                input.setText("");
            }
        }

    }
    /**
     * ActionListener for the button, used to start or continue the game
     */
    public class upbutton implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (initialize==0){
                up.setText("Next");
                input.requestFocusInWindow();
                String selectedValue = (String) select.getSelectedItem();
                int x =Integer.parseInt(selectedValue);
                X = x;
                Random random = new Random();
                int n = 1 + random.nextInt(12);
                N = n;
                label.setText(""+x+"*"+n+"  ");
                answer = x*n;
                initialize = 1;
                right.setText("Rating");
                left.setText("Answer");
            }
            else
                checkInput();
        }
    }

}