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
    private String priorityIngredient;

    /**
     * Constructor of SearchCriteria object
     *
     * @param prepTime The preparation time of the dish
     * @param ingredients Ingredients which the user has
     * @param priorityIngredient Priority Ingredient
     */
    public SearchCriteria(int prepTime, ArrayList<String> ingredients, ArrayList<String> allergens,
                          String priorityIngredient)
    {
        this.prepTime = prepTime;
        this.ingredients = ingredients;
        this.allergens = allergens;
        this.priorityIngredient = priorityIngredient;
    }

    public int getPrepTime() {return prepTime;}
    public ArrayList<String> getIngredients() {return ingredients;}
    public ArrayList<String> getAllergens() { return allergens;}
    public String getPriorityIngredient() {return priorityIngredient;}
}
