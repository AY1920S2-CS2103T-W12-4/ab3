package seedu.address.model.recipe;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.recipe.attribute.Ingredient;

/**
 * Represents a Recipe's ingredients in the recipe book.
 */
public class IngredientList {

    public static final String MESSAGE_CONSTRAINTS =
            "Ingredients can take any values for now, and it should not be " + "blank";

    /*
     * The first character of the ingredients must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final List<Ingredient> ingredients;

    // TODO: change to List, for now using String to temp store ingredient list
    public final String ingredientListString;

    /**
     * Constructs an {@code IngredientList}.
     */
    public IngredientList(String ingredientListString) {
        requireNonNull(ingredientListString);
        checkArgument(isValidIngredients(ingredientListString), MESSAGE_CONSTRAINTS);

        this.ingredientListString = ingredientListString;

        // TODO: Update IngredientList to use arraylist instead of raw String
        this.ingredients = new ArrayList<Ingredient>();
    }

    public IngredientList(List<Ingredient> ingredients) {
        this.ingredientListString = "";
        this.ingredients = ingredients;
    }

    /**
     * Returns true if a given string is a valid ingredient list.
     */
    public static boolean isValidIngredients(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Adds an ingredient to the ingredient list.
     *
     * @param ingredient the ingredient to be added.
     */
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    /**
     * Deletes an ingredient from the ingredient list.
     *
     * @param ingredient the ingredient to be deleted.
     */
    public void deleteIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    @Override
    public String toString() {
        return ingredientListString;
    }

    // TODO: delete this since we are not printing to console.

    /**
     * Prints out the ingredients list.
     */
    public void print() {
        for (int i = 0; i < ingredients.size(); i++) {
            System.out.print(ingredients.get(i));
        }
    }
}
