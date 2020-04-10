package cookbuddy.model.recipe.attribute;

import static cookbuddy.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a Recipe's ingredients in the recipe book.
 */
public class IngredientList {
    public final ArrayList<Ingredient> ingredientData;

    /**
     * Constructs an ingredient list from {@code ingredientList}. Requires that the
     * parameter be non-null. The parameter is guaranteed to be <em>valid</em>, as
     * every item in the list has already been individually checked by
     * {@link Ingredient#Ingredient(String)}.
     *
     * @param ingredientList a {@link List} of {@link Ingredient}s
     */
    public IngredientList(List<Ingredient> ingredientList) {
        requireAllNonNull(ingredientList);

        this.ingredientData = new ArrayList<Ingredient>(ingredientList);
    }

    /**
     * Adds an ingredient to the ingredient list.
     *
     * @param ingredient the ingredient to be added.
     */
    public void addIngredient(Ingredient ingredient) {
        ingredientData.add(ingredient);
    }

    /**
     * Deletes an ingredient from the ingredient list.
     *
     * @param ingredient the ingredient to be deleted.
     */
    public void deleteIngredient(Ingredient ingredient) {
        ingredientData.remove(ingredient);
    }

    public List<Ingredient> asList() {
        return List.copyOf(this.ingredientData);
    }

    @Override
    public boolean equals(Object other) {

        return (other == this || other instanceof IngredientList
                && ingredientData.equals(((IngredientList) other).ingredientData));
    }

    @Override
    public int hashCode() {
        return ingredientData.hashCode();
    }

    @Override
    public String toString() {
        return ingredientData.stream().map(Ingredient::toString).collect(Collectors.joining(";"));
    }
}
