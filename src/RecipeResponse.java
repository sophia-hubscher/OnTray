/**
 * @author Vishal Chandra
 *
 * Object which holds information about recipe results from APIClient
 */
public class RecipeResponse
{
    private int prepTime;
    private String description;
    private String ingredients;
    private String directions;

    /**
     * Constructor
     * @param prepTime preparation time of dish
     * @param description description of dish
     * @param ingredients ingredients of dish
     * @param directions preparation instructions of dish
     */
    public RecipeResponse(int prepTime, String description, String ingredients, String directions)
    {
        this.prepTime = prepTime;
        this.description = description;
        this.ingredients = ingredients;
        this.directions = directions;
    }

    //getters
    public int getPrepTime() {return prepTime;}
    public String getDescription() {return description;}
    public String getIngredients() {return ingredients;}
    public String getDirections() {return directions;}
}
