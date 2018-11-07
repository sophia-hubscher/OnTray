import java.util.ArrayList;

/**
 * Data structure sent to APIClient
 *
 * @author Vishal Chandra
 */
public class SearchCriteria
{
    private int prepTime;
    private ArrayList<String> ingredients;
    private ArrayList<String> allergens;
    private String mustHaveIngredient;

    /**
     * Constructor of SearchCriteria object
     *
     * @param prepTime The preparation time of the dish
     * @param ingredients Ingredients which the user has
     * @param mustHaveIngredient Priority Ingredient
     */
    public SearchCriteria(int prepTime, ArrayList<String> ingredients, ArrayList<String> allergens,
                          String mustHaveIngredient)
    {
        this.prepTime = prepTime;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.mustHaveIngredient = mustHaveIngredient;
    }

    public int getPrepTime() {return prepTime;}
    public ArrayList<String> getIngredients() {return ingredients;}
    public ArrayList<String> getAllergens() { return allergens;}
    public String getMustHaveIngredient() {return mustHaveIngredient;}
}
