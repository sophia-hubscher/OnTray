import com.fatsecret.platform.model.*;
import com.fatsecret.platform.services.FatsecretService;
import com.fatsecret.platform.services.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface with fatsecret api
 *
 * @author Vishal Chandra
 */
public class APIClient
{

    final String key = "7787620047b043238c5544c8b94deb48";
    final String sharedSecret = "3ee99aafb76b41179579cc7735786000";
    FatsecretService service;

    /**
     * main method; uses information in SearchCriteria parameter to perform a refined search of the fatsecret API
     * @param searchCriteria SearchCriteria object which specifies searching parameters
     * @return
     */
    public ArrayList<RecipeResponse> search(SearchCriteria searchCriteria)
    {
        //init api
        service = new FatsecretService(key, sharedSecret);

        List<CompactRecipe> compactRecipes = searchByIngredients(searchCriteria.getIngredients());
        ArrayList<Recipe> recipes = expandRecipes(compactRecipes);
        filterByPrepTime(recipes, searchCriteria.getPrepTime());

        ArrayList<RecipeResponse> responses = new ArrayList<RecipeResponse>();
        for(Recipe recipe : recipes)
        {
            String ingredientsString = "";
            for(Ingredient ingredient : recipe.getIngredients())
            {
                ingredientsString += ingredient.getName() + "\n";
            }

            String directionsString = "";
            for(Direction direction : recipe.getDirections())
            {
                directionsString += direction.getDescription() + "\n";
            }

            responses.add(new RecipeResponse(recipe.getPreparationTime(), recipe.getDescription(), ingredientsString,
                                directionsString));
        }

        return responses;
    }


    /**
     * Refines ArrayList<Recipe> by removing Recipe objects which exceed the desired preparation time
     * @param recipes ArrayList to act on
     * @param desiredPrepTime preparation time limit
     */
    public void filterByPrepTime(ArrayList<Recipe> recipes, int desiredPrepTime)
    {
        double bufferedPrepTime = desiredPrepTime * 1.1;
        int roundedPrepTime = (int) Math.ceil(bufferedPrepTime);

        for (int i = 0; i < recipes.size(); i++)
        {
            if(recipes.get(i).getPreparationTime() == null || recipes.get(i).getPreparationTime() > roundedPrepTime)
            {
                recipes.remove(i);
                i--;
            }
        }
    }


    /**
     * Produces a list of CompactRecipes by searching the API for the provided ingredients
     * @param queryIngredients ArrayList<String> of ingredients to search for
     * @return List<CompactRecipe> Recipes
     */
    public List<CompactRecipe> searchByIngredients(ArrayList<String> queryIngredients)
    {
        String query = "";
        for(String queryIngredient : queryIngredients)
        {
            query += queryIngredient + ", ";
        }
        query = query.substring(0, query.length() - 2);

        Response<CompactRecipe> response = service.searchRecipes(query);

        try
        {
            return response.getResults();
        }
        catch(NullPointerException npe){return null;}
    }


    /**
     * (deprecated)
     * Removes recipes without the specified ingredient from the recipe list
     * @param recipes the recipe list to act on
     * @param priorityIngredient the ingredient to search for
     */
    public void filterByPriorityIngredient(ArrayList<Recipe> recipes, String priorityIngredient)
    {
        for (int i = 0; i < recipes.size(); i++)
        {
            List<Ingredient> ingredients = recipes.get(i).getIngredients();

            boolean containsIngredient = false;
            for(Ingredient ingredient : ingredients)
            {
                if(ingredient.getName() == priorityIngredient) containsIngredient = true;
            }

            if(!containsIngredient)
            {
                recipes.remove(i);
                /*after removing the object at index i, the object with index i+1 moves to location i
                  if we then move the index to i+1, we miss this next object. so before moving to the
                  next iteration of the loop we subtract 1 from i
                 */
                i--;
            }
        }
    }

    /**
     * Converts a ArrayList<CompactRecipe> object to a ArrayList<Recipe> object
     *
     * @param compactRecipes the ArrayList<CompactRecipe> object to convert
     * @return a ArrayList<Recipe> object
     */
    public ArrayList<Recipe> expandRecipes(List<CompactRecipe> compactRecipes)
    {
        ArrayList<Recipe> expandedRecipes = new ArrayList<>();

        for (CompactRecipe recipe : compactRecipes)
        {
            long recipeID = recipe.getId();
            Recipe expandedRecipe = service.getRecipe(recipeID);

            //sometime getRecipe(ID) returns null
            if (expandedRecipe != null) expandedRecipes.add(expandedRecipe);
        }

        return expandedRecipes;
    }
}
