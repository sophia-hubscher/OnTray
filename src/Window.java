import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Window holds all buttons, labels, etc
 *
 * @author Sophia Hübscher
 * @version 1.0
 */
public class Window extends JFrame implements ActionListener, KeyListener
{
    JPanel      masterPanel, showTextPanel;
    JTextArea   showText;
    JScrollPane outputScroll;

    APIClient apiClient;
    ArrayList<RecipeResponse> recipeResponses;

    Timer t;
    Display display;

    SearchCriteria searchCriteria;

    int delayTime = 10; //how long before the timer fires (ms)
    int width;
    int height;

    /**
     * Creates master panel & smaller panels with all JComponents
     *
     * @param width  width of screen
     * @param height height of screen
     */
    public Window(int width, int height)
    {
        this.width = width - 440;
        this.height = height;

        this.setResizable(false);

        apiClient = new APIClient();

        //master panel holds all other panels
        masterPanel = (JPanel) this.getContentPane();
        masterPanel.setLayout(new BorderLayout());

        //adds key listener
        addKeyListener(this);

        //makes a display object that will show board and motion
        display = new Display(width, height, "Helvetica");
        display.deserializeArrayList("allergies.txt");
        //area where text is displayed
        showText = new JTextArea("", 27, 50); //size of text area in chars
        showText.setForeground(display.TEXT_COLOR);
        Font fontBody = new Font("Helvetica", Font.PLAIN, 20);
        showText.setBackground(display.BACKGROUND);
        showText.setFont(fontBody);
        showText.setEditable(false);
        showText.setLineWrap(true);
        showText.setWrapStyleWord(true);

        //adds the report to a scroll pane, so if it gets big, it will scroll
        outputScroll = new JScrollPane(showText);
        outputScroll.createVerticalScrollBar();

        //adds the scroll pane to the overall panel
        showTextPanel = new JPanel();
        showTextPanel.add(outputScroll);
        showTextPanel.setBackground(display.BACKGROUND); //sets color

        //creating timer
        t = new Timer(delayTime, this);
        t.addActionListener(this);
        t.start();

        //adding display to panel
        masterPanel.add(display, BorderLayout.CENTER);
        masterPanel.add(showTextPanel, BorderLayout.EAST);

        //sets title and size of window and set to be visible in the JFrame
        this.setTitle("ontray");
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Controls all responses to actions
     *
     * @param e the specific event
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == t) //when timer fires
        {
            //if meal is being displayed, show recipe
            if (display.scene.equals("Meal")) showTextPanel.setVisible(true);
            else showTextPanel.setVisible(false);

            //if no meal, show other scene
            if (display.scene.equals("Meal"))
            {
                //make search criteria object
                searchCriteria = new SearchCriteria(display.prepTime, display.ingredients,
                        display.allergens, display.mustHaveIngredient);

                //loading symbol while user waits for recipe
                display.cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);

                recipeResponses = apiClient.search(searchCriteria);

                //changing cursor back after apiClient.search() has finished
                display.cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

                if (recipeResponses.size() == 0) display.scene = "Sorry";

                //display meal text
                String recipeText = recipeResponses.get(0).getDescription() + "\n\n" +
                        "Prep time: " + recipeResponses.get(0).getPrepTime() + " minutes\n\n" +
                        "Ingredients: \n" +
                        recipeResponses.get(0).getIngredients() + "\n\n" +
                        "Directions: \n" + recipeResponses.get(0).getDirections();

                showText.setText(recipeText);
            }

            //repaint display
            display.repaint();
        }
    }

    /**
     * Displays text when key is typed
     *
     * @param e event that shows which key has been typed
     */
    public void keyTyped(KeyEvent e)
    {
        char lastKey = e.getKeyChar();

        if (display.scene.equals("Time"))
        {
            if (Character.isDigit(lastKey))
            {
                if (display.currentText.equals("  :  "))
                {
                    display.currentText = lastKey + " :  ";
                } else if (display.currentText.endsWith(" :  "))
                {
                    display.currentText = display.currentText.substring(0, 1) + lastKey + ":  ";
                } else if (display.currentText.endsWith(":  "))
                {
                    if (!(Integer.parseInt(lastKey + "") > 5))
                        display.currentText = display.currentText.substring(0, 3) + lastKey + " ";
                } else if (display.currentText.endsWith(" "))
                {
                    display.currentText = display.currentText.substring(0, 4) + lastKey;
                }
            }
        } else
        {
            if (Character.isAlphabetic(lastKey) || lastKey == ' ') display.currentText += lastKey;
        }
    }

    /**
     * Controls deletion and submission of typed words
     *
     * @param e event that shows which key has been pressed
     */
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            if (display.currentText.length() > 1)
            {
                display.currentText = display.currentText.substring(0, display.currentText.length() - 2);
            } else if (display.currentText.length() == 1)
            {
                display.currentText = "";
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if (display.scene.equals("Ingredients"))
            {
                display.ingredients.add(display.currentText);
                    display.currentText = "";
            } else if (display.scene.equals("Allergies"))
            {
                display.allergens.add(display.currentText);
                display.currentText = "";
                display.serializeArrayList(display.allergens, "allergies.txt");
            } else if (display.scene.equals("Feature"))
            {
                if (!(display.currentText.equals("") || display.currentText.equals(null)))
                {
                    display.mustHaveIngredient = display.currentText;
                    display.currentText = "  :  ";
                    display.scene = "Time";
                }
            } else if (display.scene.equals("Time"))
            {
                if(!(display.currentText.equals("") || display.currentText.equals(null)))
                {
                    display.prepTime = Integer.parseInt(display.currentText.substring(0, 2)) * 60 +
                            Integer.parseInt(display.currentText.substring(3, 5));
                    display.scene = "Meal";
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {}
}