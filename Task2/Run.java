import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

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
     * The label at the top, displaying the current question.
     */
    public JLabel label;
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
     * The text field where the user enters their answers.
     */
    public JTextField input;
    /**
     * The list from which the user can select numbers for the game.
     */
    public JList<String> list;
    /**
     * The scroll pane that contains the list, allowing for scrolling if necessary.
     */
    public JScrollPane scrollPane; // Scrollbar
    private static int Counter, initialize, corr; // Counter, initialization, correct count
    /**
     * A list of numbers used in the game's multiplication problems.
     */
    public static List<Integer> listN = new ArrayList<>(); // Store numbers
    /**
     * A list of numbers used in the game's multiplication problems.
     */
    public static List<Integer> listX = new ArrayList<>(); // Store numbers
    /**
     * A list of expected results for the game's multiplication problems.
     */
    public static List<Integer> listA = new ArrayList<>(); // Store numbers
    /**
     * The label displaying the timer during the game.
     */
    private JLabel timerLabel; // Timer
    /**
     * The timer used to track the time taken by the user to answer questions.
     */
    private Timer gameTimer; // Timer
    /**
     * The variable storing the current number of seconds for the timer.
     */
    private int seconds = 0; // Store seconds
    /**
     * An array of selectable numbers for the multiplication game.
     */
    public String[] ct = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}; // Selectable list

    /**
     * The entry point of the game. It creates an instance of the Run class and starts the game.
     *
     * @param args The command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        Run gui = new Run();
        gui.go();
    }

    /**
     * Create and display the game interface with all its components.
     */
    public void go() {
        // Initialization and setup of the game interface components.
        frame = new JFrame("SuperHero Times Tables(enhanced version)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Generate various components and initialize them
        timerLabel = new JLabel("Time: 0s");
        nor = new JPanel();
        left = new JLabel("Answer");
        right = new JLabel("Rating");
        input = new JTextField(7);
        label = new JLabel("Product:  ");
        up = new JButton("Start");
        list = new JList<>();
        list.setListData(ct);
        list.setVisibleRowCount(3);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);

        // Add monitors to various components
        up.addActionListener(new upbutton());
        input.addKeyListener(new Check());
        list.addListSelectionListener(new Sel());
        gameTimer = new Timer(1000, new TIME());

        // Add images
        ImageIcon image = new ImageIcon("superhero.jpg");
        JLabel label2 = new JLabel(image); // Use the image to construct a JLabel
        // Create a scaled image
        Image scaledImage = image.getImage().getScaledInstance(300, 250, Image.SCALE_SMOOTH); // Resize the image to 300x200
        label2.setIcon(new ImageIcon(scaledImage)); // Set the scaled image as the icon of the JLabel

        // Create a vertical Box to organize components
        Box vBox = Box.createVerticalBox();
        // First add select to the vertical layout
        vBox.add(scrollPane);
        vBox.add(Box.createVerticalStrut(10)); // Add some vertical spacing
        // Create a horizontal Box to organize label, input, and up
        Box hBox = Box.createHorizontalBox();
        hBox.add(label);
        hBox.add(input);
        hBox.add(up);
        // Then add the horizontal Box to the vertical layout
        vBox.add(hBox);
        // Add the vertical Box to the panel nor

        // Set the display
        nor.add(vBox);
        frame.getContentPane().add(BorderLayout.EAST, right);
        frame.getContentPane().add(BorderLayout.CENTER, label2);
        frame.getContentPane().add(BorderLayout.NORTH, nor);
        frame.getContentPane().add(BorderLayout.WEST, left);
        //frame.getContentPane().add(BorderLayout.SOUTH, timerLabel);
        frame.setSize(500, 450);
        frame.setVisible(true);
    }

    /**
     * Method for checking the user's input against the expected value.
     * Updates the game state and the interface based on the result.
     */
    public void checkInput() {
        try {
            String text = input.getText();
            if (!text.isEmpty()) {
                int userValue = Integer.parseInt(text); // Attempt to convert the text to an integer
                int expectedValue = listA.get(Counter);
                if (userValue == expectedValue) {
                    // User input is correct
                    left.setText("Correct");
                    corr++; // Increase the count of correct answers
                } else {
                    // User input is wrong
                    left.setText("Wrong! " + listX.get(Counter) + "*" + listN.get(Counter) + " = " + listA.get(Counter));
                }
                Counter++; // Increase the number of answered questions
                if (Counter == 5) { // Play five rounds of the game and reset the game
                    gameTimer.stop(); // Stop the timer
                    right.setText("You got " + corr + " correct!");
                    up.setText("Start");
                    Counter = 0; // Reset the counter
                    corr = 0; // Reset the correct count
                    initialize = 0; // Reset the label
                    input.setText("");
                    label.setText("Product:  ");
                    // Reset the timer's seconds
                    timerLabel.setText("You took " + seconds + " s");
                } else {// Not yet played five rounds, continue
                    label.setText(listX.get(Counter) + " * " + listN.get(Counter) + "   ");
                    input.setText("");
                }
            } else {
                // Text box is empty
                JOptionPane.showMessageDialog(frame, "Please enter a number!");
                input.setText("");
            }
        } catch (NumberFormatException nfe) {
            // User input is not a number
            JOptionPane.showMessageDialog(frame, "Please enter a valid number!");
            input.setText("");
        }
    }

    /**
     * Key listener for the input text field that triggers the input check
     * when the user presses the Enter key.
     */
    public class Check extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (initialize == 1)) {
                checkInput();
            }
            if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (initialize == 0)) {
                JOptionPane.showMessageDialog(frame, "Please click the Start button to begin the game!");
                input.setText("");
            }
        }
    }




    /**
     * Action listener for the 'up' button that starts the game or proceeds to the next question.
     */
    public class upbutton implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (initialize==0){//Initialization stage
                // Get the selection model of JList
                ListSelectionModel selectionModel = list.getSelectionModel();
                if(selectionModel.isSelectionEmpty()){//List selection is empty
                    JOptionPane.showMessageDialog(frame, "Please select at least one");
                }
                else {//List selection is not empty
                    initialize = 1;//Mode selection, game has been initialized
                    input.requestFocusInWindow();//Set focus to text box
                    up.setText("Next");//Set button label
                    right.setText("Rating");//Set right label
                    left.setText("Answer");//Set left label
                    seconds = 0; // Reset seconds
                    gameTimer.start(); // Start the timer
                    // Initialize listN
                    listN.clear(); // Clear listN to prepare for new random numbers
                    Random random = new Random(); // Create a random number generator
                    while (listN.size() < 5) { // Until there are 5 numbers in listN
                        int randomNumber = random.nextInt(12) + 1; // Generate a random integer from 1 to 12
                        listN.add(randomNumber); // Add the random number to listN
                    }
                    // Print listN to verify the result
                    // System.out.println("listN contains: " + listN);

                    // Initialize listA
                    listA.clear();
                    // Iterate through the list and multiply the corresponding elements
                    for (int i = 0; i < listN.size(); i++) {
                        int product = listN.get(i) * listX.get(i);
                        listA.add(product); // Add the product to listA
                    }
                    // Print listA to verify the result
                    // System.out.println("listA contains: " + listA);

                    label.setText("" + listX.get(0) + " * " + listN.get(0) + "  ");//Set initial label
                }
            }
            else//Not in the initialization stage, directly into the check
                checkInput();
        }
    }


    /**
     * List selection listener for the number selection list.
     * Manages the selection of numbers by the user before the game starts.
     */
    public class Sel implements ListSelectionListener {
        // Use ArrayList to store selected items
        private List<String> selectedItems = new ArrayList<>();
        private Random random = new Random(); // Create a random number generator

        public void valueChanged(ListSelectionEvent e) {
            // Check if it's adjusting selection, not scrolling
            if (!e.getValueIsAdjusting()) {//Click action occurred
                if (initialize == 0) { // Game initialization stage, selection is allowed
                    // Clear previously stored selected items
                    selectedItems.clear();
                    // Read selected values
                    ListSelectionModel selectionModel = list.getSelectionModel();
                    int minIndex = selectionModel.getMinSelectionIndex();
                    int maxIndex = selectionModel.getMaxSelectionIndex();
                    // Iterate through all selected items and add them to the selectedItems list
                    if (minIndex >= 0) {
                        for (int i = minIndex; i <= maxIndex && i < ct.length; i++) {
                            if (selectionModel.isSelectedIndex(i)) {
                                selectedItems.add(ct[i]);
                                System.out.println("Selected: " + ct[i]);
                            }
                        }
                    }
                    // Make sure selectedItems has enough elements to choose from
                    if (selectedItems.size() > 0) {
                        // Clear listX to prepare for storing new random numbers
                        listX.clear();
                        // Randomly select five numbers from selectedItems and store them in listX
                        for (int i = 0; i < 5; i++) {
                            int randomIndex = random.nextInt(selectedItems.size());
                            String selectedItem = selectedItems.get(randomIndex);
                            // Convert selectedItem to Integer and add it to listX
                            listX.add(Integer.parseInt(selectedItem));//Added to listX are all integers
                        }
                        // Print listX to verify the result
//                        System.out.println("listX contains: " + listX);
                    }
                    else
                        System.out.println("No items selected.");

                }
                else if (initialize == 1) {//Game has been initialized, prompt appears
                    // Game is in progress, selection is not allowed
                    JOptionPane.showMessageDialog(frame, "Cannot be re-selected during gameplay!");
                }
            }
        }
    }


    /**
     * Timer listener that updates the timer label every second.
     */
    private class TIME implements ActionListener { // Triggered every 1000 milliseconds
        public void actionPerformed(ActionEvent e) {
            seconds++; // Increase one second per second
            timerLabel.setText("Time: " + seconds + "s"); // Update the timer label
        }
    }
}