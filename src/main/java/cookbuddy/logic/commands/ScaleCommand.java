package cookbuddy.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import cookbuddy.commons.core.Messages;
import cookbuddy.commons.core.index.Index;
import cookbuddy.logic.commands.exceptions.CommandException;
import cookbuddy.model.Model;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.model.recipe.attribute.Ingredient;
import cookbuddy.model.recipe.attribute.IngredientList;
import cookbuddy.model.recipe.attribute.Name;
import cookbuddy.model.recipe.attribute.Quantity;

/**
 * Adds a duplicate of the recipe identified using it's displayed index from the recipe book.
 */
public class ScaleCommand extends Command {

    public static final String COMMAND_WORD = "scale";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": A duplicate of the recipe identified by the index number is added to the recipe list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1" + " 2";

    public static final String MESSAGE_SCALE_RECIPE_SUCCESS = "Recipe scaled: %1$s %1$s";

    private final Index targetIndex;
    private final int size;

    public ScaleCommand(Index targetIndex, int size) {
        this.targetIndex = targetIndex;
        this.size = size;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Recipe> lastShownList = model.getFilteredRecipeList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
        }

        Recipe recipeToScale = lastShownList.get(targetIndex.getZeroBased());

        String name = recipeToScale.getName().getName();
        String newName = "Scaled " + name;
        Name nameOfScaledRecipe = new Name(newName);

        List<Ingredient> ingredients = recipeToScale.getIngredients().asList();
        List<Ingredient> scaledIngredients = new ArrayList<>();
        for (Ingredient ingredient: ingredients) {
            Quantity quantity2 = ingredient.getQuantity2();
            Quantity scaledQuantity2 = quantity2.scaledQuantity(size);
            String ingredientName = ingredient.getName();
            String scaledQuantity = ingredientName + ", " + scaledQuantity2.stringRep();
            Ingredient scaledIngredient = new Ingredient(scaledQuantity);
            scaledIngredients.add(scaledIngredient);
        }
        IngredientList scaledIngredientList = new IngredientList(scaledIngredients);

        ModifyCommand.EditRecipeDescriptor editRecipeDescriptor = new ModifyCommand.EditRecipeDescriptor();
        editRecipeDescriptor.setName(nameOfScaledRecipe);
        editRecipeDescriptor.setIngredients(scaledIngredientList);
        Recipe scaledRecipe = ModifyCommand.createEditedRecipe(recipeToScale, editRecipeDescriptor);

        model.addRecipe(scaledRecipe);
        return new CommandResult(String.format(MESSAGE_SCALE_RECIPE_SUCCESS, recipeToScale));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScaleCommand // instanceof handles nulls
                && targetIndex.equals(((ScaleCommand) other).targetIndex)); // state check
    }
}












