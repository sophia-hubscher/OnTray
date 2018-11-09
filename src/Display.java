import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Displays GUI for user to interact with
 *
 * @author Sophia Hübscher
 * @version 1.0
 */
public class Display extends JPanel implements MouseMotionListener, MouseListener
{
    Font f;

    int width;
    int height;
    String scene = "Home";
    String currentText = "";
    String fontName;

    int prepTime;
    ArrayList<String> ingredients;
    ArrayList<String> allergens;
    String mustHaveIngredient;

    Color TEXT_COLOR = new Color(168, 168, 168);
    Color BACKGROUND = new Color(253, 253, 253);
    BufferedImage image;
    Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    /**
     * Displays GUI for user to interact with
     *
     * @param width    width of screen
     * @param height   height of screen
     * @param fontName name of font used
     */
    public Display(int width, int height, String fontName)
    {
        //set up frame for display
        this.width = width;
        this.height = height;
        this.fontName = fontName;

        //react to mouse motion and clicks
        addMouseMotionListener(this);
        addMouseListener(this);

        //sets fonts
        f = new Font(fontName, Font.PLAIN, 50);

        //initialize arraylists
        ingredients = new ArrayList<String>();
        allergens   = new ArrayList<String>();
    }

    /**
     * Draws all graphics by a timer calling repaint()
     *
     * @param g the graphics object, automatically sent when repaint() is called
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); //clear the old drawings

        //improves graphics
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //sets cursor to either default, hand, or text cursor
        this.setCursor(cursor);

        //sets basic fonts
        g.setFont(f);

        //sets background image to current scene
        try
        {
            image = ImageIO.read(new File("res/" + scene + ".png"));
        } catch (IOException ex)
        {
            System.out.println("error setting image");
        }

        //draws background image
        g.drawImage(image, 0, 0, width, height, this);

        g.setColor(TEXT_COLOR);

        //draws text
        currentText = currentText.toLowerCase();
        
        //prevents user's text from being too long
        if (currentText.length() > 22) currentText = currentText.substring(0, 22);

        if (scene.equals("Ingredients") || scene.equals("Allergies"))
        {
            g.drawString(currentText, 76, 300);
        } else if (scene.equals("Feature"))
        {
            g.drawString(currentText, 139, 300);
        } else if (scene.equals("Time"))
        {
            //text may not be longer than 5 characters
            if (currentText.length() > 4)
                currentText = currentText.substring(0, 5);
            
            //draws the time the user is typing
            drawCenteredString(g, currentText.toLowerCase(),
                    new Rectangle(0, 310, width, 0),
                    new  Font(fontName, Font.PLAIN, 200));
        }
    }

    /**
     * Checks mouse location and sets toolTipText accordingly
     *
     * @param e specific mouse event
     */
    public void mouseMoved(MouseEvent e)
    {
        int xLoc = e.getX();
        int yLoc = e.getY();

        //check x and y locations and set toolTipText & cursor based off
        //of them
        if (scene.equals("Home"))
        {
            if (xLoc > 70 && xLoc < 70 + 385.96 && yLoc > 253.74
                    && yLoc < 253.74 + 185.26)
            { //new recipe
                this.setToolTipText("new recipe");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else if (xLoc > 535 && xLoc < 535 + 385.96 && yLoc > 255.74
                    && yLoc < 255.74 + 185.26)
            { //allergies
                this.setToolTipText("allergies");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else
            { //no tool tip text & default cursor
                this.setToolTipText("");
                cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            }
        } else if (scene.equals("Ingredients"))
        {
            if (xLoc > 76 && xLoc < 76 + 800 && yLoc > 181 && yLoc < 308)
            { //text bar
                this.setToolTipText("ingredients");
                cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
            } else if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                this.setToolTipText("home");
                currentText = "";
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else if (xLoc > 350 && xLoc < 350 + 300 && yLoc > 359
                    && yLoc < 359 + 70)
            { //next step button
                this.setToolTipText("next step");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else if (xLoc > 894 && xLoc < 894 + 50 && yLoc > 255
                    && yLoc < 255 + 50)
            { //add ingredient button
                this.setToolTipText("add");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else
            { //no tool tip text & default cursor
                this.setToolTipText("");
                cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            }
        } else if (scene.equals("Feature"))
        {
            if (xLoc > 76 && xLoc < 76 + 800 && yLoc > 181 && yLoc < 308)
            { //text bar
                this.setToolTipText("ingredients");
                cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
            } else if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                this.setToolTipText("home");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else if (xLoc > 350 && xLoc < 350 + 300 && yLoc > 359
                    && yLoc < 359 + 70)
            { //next step button
                this.setToolTipText("next step");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else
            { //no tool tip text & default cursor
                this.setToolTipText("");
                cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            }
        } else if (scene.equals("Time"))
        {
            if (xLoc > 70 && xLoc < 70 + 385.96 && yLoc > 253.74
                    && yLoc < 253.74 + 185.26)
            { //home button
                this.setToolTipText("home");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else if (xLoc > 350 && xLoc < 350 + 300 && yLoc > 451
                    && yLoc < 451 + 70)
            { //next step button
                this.setToolTipText("next step");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else if (xLoc > 234.92 && xLoc < 234.92 + 529.32 && yLoc > 244
                    && yLoc < 391.32)
            { //time line
                this.setToolTipText("time");
                cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
            } else
            { //no tool tip text & default cursor
                this.setToolTipText("");
                cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            }
        } else if (scene.equals("Meal"))
        {
            if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                && yLoc < 16.34 + 42.66)
            { //home button
                this.setToolTipText("home");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else
            { //no tool tip text & default cursor
                this.setToolTipText("");
                cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            }
        } else if (scene.equals("Sorry"))
        {
            if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                this.setToolTipText("home");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else
            { //no tool tip text & default cursor
                this.setToolTipText("");
                cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            }
        } else if (scene.equals("Allergies"))
        {
            if (xLoc > 76 && xLoc < 76 + 800 && yLoc > 181 && yLoc < 308)
            { //text bar
                this.setToolTipText("allergens");
                cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
            } else if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                this.setToolTipText("home");
                currentText = "";
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else if (xLoc > 350 && xLoc < 350 + 300 && yLoc > 359
                    && yLoc < 359 + 70)
            { //next step button
                this.setToolTipText("all set");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else if (xLoc > 894 && xLoc < 894 + 50 && yLoc > 255
                    && yLoc < 255 + 50)
            { //add ingredient button
                this.setToolTipText("add");
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            } else
            { //no tool tip text & default cursor
                this.setToolTipText("");
                cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            }
        }
    }

    /**
     * Reacts based on where the users mouse is clicked
     *
     * @param e specific action being acted upon
     */
    public void mouseClicked (MouseEvent e)
    {
        int xLoc = e.getX();
        int yLoc = e.getY();

        //check x and y locations and react based off of them

        if (scene.equals("Home"))
        {
            if (xLoc > 70 && xLoc < 70 + 245.12 && yLoc > 234.74
                        && yLoc < 234.74 + 185.26)
            { //new recipe
                scene = "Ingredients";
            } else if (xLoc > 535 && xLoc < 535 + 385.96 && yLoc > 255.74
                    && yLoc < 255.74 + 185.26)
            { //allergies
                scene = "Allergies";
            }
        }
        else if (scene.equals("Ingredients"))
        {
            if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                goToNextScreen("Home");
            }
            else if (xLoc > 350 && xLoc < 350 + 300 && yLoc > 359
                    && yLoc < 359 + 70)
            { //next step button
                goToNextScreen("Feature");
            }
            else if (xLoc > 894 && xLoc < 894 + 50 && yLoc > 255
                    && yLoc < 255 + 50)
            { //add ingredient button
                ingredients.add(currentText);
                currentText = "";
            }
        }
        else if (scene.equals("Feature"))
        {
            if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                goToNextScreen("Home");
            }
            else if (xLoc > 350 && xLoc < 350 + 300 && yLoc > 359
                && yLoc < 359 + 70 && !(currentText.equals("") ||
                currentText.equals(null)))
            { //next step button
                mustHaveIngredient = currentText;
                currentText = "  :  ";
                scene = "Time";
            }
        }
        else if (scene.equals("Time"))
        {
            if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                goToNextScreen("Home");
            }
            else if (xLoc > 350 && xLoc < 350 + 300 && yLoc > 451
                    && yLoc < 451 + 70 && !(currentText.equals("") ||
                    currentText.equals(null)))
            { //next step button
                prepTime = Integer.parseInt(currentText.substring(0, 2)) * 60 +
                        Integer.parseInt(currentText.substring(3, 5));
                scene = "Meal";
            }
        }
        else if (scene.equals("Meal"))
        {
            if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                goToNextScreen("Home");
            }
        }
        else if (scene.equals("Sorry"))
        {
            if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                goToNextScreen("Home");
            }
        }
        else if (scene.equals("Allergies"))
        {
            if (xLoc > 17 && xLoc < 17 + 39.58 && yLoc > 16.34
                    && yLoc < 16.34 + 42.66)
            { //home button
                goToNextScreen("Home");
            }
            else if (xLoc > 350 && xLoc < 350 + 300 && yLoc > 359
                    && yLoc < 359 + 70)
            { //next step button
                goToNextScreen("Home");
                serializeArrayList(allergens, "allergies.txt");
            }
            else if (xLoc > 894 && xLoc < 894 + 50 && yLoc > 255
                    && yLoc < 255 + 50)
            { //add allergen button
                allergens.add(currentText);
                currentText = "";
            }
        }
    }
    
    
    /**
     * Goes to next screen & clears currentText
     */
    public void goToNextScreen(String sceneName)
    {
        currentText = "";
        scene = sceneName;
    }

    /**
     * Draws string centered in rectangle
     *
     * @param g graphics object
     * @param text text to center
     * @param rect borders
     * @param font font of text
     */
    public void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {

        FontMetrics metrics = g.getFontMetrics(font);

        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);
        g.drawString(text, x, y);
    }

    /**
     * Save ArrayList to a txt file
     *
     * @param list ArrayList to be saved
     * @param fileName name of file being saved
     */
    public void serializeArrayList (ArrayList<String> list, String fileName)
    {
        try
        {
            FileOutputStream fos= new FileOutputStream(fileName);
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            oos.writeObject(list);
            oos.close();
            fos.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    /**
     * deserialize the allergens ArrayList from txt file
     *
     * @param fileName name of file
     */
    public void deserializeArrayList (String fileName)
    {
        try
        {
            FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            allergens = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        }
        catch(ClassNotFoundException cnfe){}
        catch(IOException ioe){}
    }

    public void mouseExited (MouseEvent e) {}
    public void mouseDragged (MouseEvent e) {}
    public void mousePressed (MouseEvent e) {}
    public void mouseEntered (MouseEvent e) {}
    public void mouseReleased (MouseEvent e) {}
}
