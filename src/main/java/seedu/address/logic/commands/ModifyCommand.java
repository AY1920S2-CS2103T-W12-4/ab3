package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CALORIE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INGREDIENTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INSTRUCTIONS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_RECIPES;

import java.util.*;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.recipe.*;
import seedu.address.model.recipe.attribute.*;
import seedu.address.model.recipe.attribute.IngredientList;
import seedu.address.model.recipe.Recipe;

/**
 * Edits the details of an existing recipe in the recipe book.
 */
public class ModifyCommand extends Command {

    public static final String COMMAND_WORD = "modify";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the recipe identified "
            + "by the index number used in the displayed recipe list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_INGREDIENTS + "INGREDIENTS] "
            + "[" + PREFIX_INSTRUCTIONS + "INSTRUCTIONS] "
            + "[" + PREFIX_CALORIE + "CALORIES] "
            + "[" + PREFIX_SERVING + "SERVING] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_INGREDIENTS + "toast,2;eggs,1 "
            + PREFIX_INSTRUCTIONS + "put egg on toast;put bread on egg";

    public static final String MESSAGE_EDIT_RECIPE_SUCCESS = "Edited Recipe: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_RECIPE = "This recipe already exists in the recipe book.";

    private final Index index;
    private final EditRecipeDescriptor editRecipeDescriptor;

    /**
     * @param index of the recipe in the filtered recipe list to edit
     * @param editRecipeDescriptor details to edit the recipe with
     */
    public ModifyCommand(Index index, EditRecipeDescriptor editRecipeDescriptor) {
        requireNonNull(index);
        requireNonNull(editRecipeDescriptor);

        this.index = index;
        this.editRecipeDescriptor = new EditRecipeDescriptor(editRecipeDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Recipe> lastShownList = model.getFilteredRecipeList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_RECIPE_DISPLAYED_INDEX);
        }

        Recipe recipeToEdit = lastShownList.get(index.getZeroBased());
        Recipe editedRecipe = createEditedRecipe(recipeToEdit, editRecipeDescriptor);

        if (!recipeToEdit.isSameRecipe(editedRecipe) && model.hasRecipe(editedRecipe)) {
            throw new CommandException(MESSAGE_DUPLICATE_RECIPE);
        }

        model.setRecipe(recipeToEdit, editedRecipe);
        model.updateFilteredRecipeList(PREDICATE_SHOW_ALL_RECIPES);
        return new CommandResult(String.format(MESSAGE_EDIT_RECIPE_SUCCESS, editedRecipe));
    }

    /**
     * Creates and returns a {@code Recipe} with the details of {@code recipeToEdit}
     * edited with {@code editRecipeDescriptor}.
     */
    private static Recipe createEditedRecipe(Recipe recipeToEdit, EditRecipeDescriptor editRecipeDescriptor) {
        assert recipeToEdit != null;

        Name updatedName = editRecipeDescriptor.getName().orElse(recipeToEdit.getName());
        IngredientList updatedIngredients = editRecipeDescriptor.getIngredients().orElse(recipeToEdit.getIngredients());
        InstructionList updatedInstructions =
                editRecipeDescriptor.getInstructions().orElse(recipeToEdit.getInstructions());
        Calorie updatedCalorie = editRecipeDescriptor.getCalorie().orElse(recipeToEdit.getCalorie());
        Serving updatedServing = editRecipeDescriptor.getServing().orElse(recipeToEdit.getServing());
        Set<Tag> updatedTags = editRecipeDescriptor.getTags().orElse(recipeToEdit.getTags());

        return new Recipe(updatedName, updatedIngredients, updatedInstructions, updatedCalorie, updatedServing,
                updatedTags);
    }

     private static Recipe scaleRecipe(Recipe recipeToScale, int sizeOfScaling) {
        ArrayList<Ingredient> ingredientData = recipeToScale.getIngredients().getIngredientData();
        for (Ingredient ingredient: ingredientData) {
            Quantity quantity2 = ingredient.getQuantity2();
            Float value = quantity2.getValue();
            Float updatedValue = value * sizeOfScaling;
            quantity2.setValue(updatedValue);
            ingredient.setQuantity2(quantity2);
         }
        //Calorie updatedCalorie =
        return new Recipe(recipeToScale.getName(), new IngredientList(ingredientData),
                recipeToScale.getInstructions(), recipeToScale.getCalorie(),
                recipeToScale.getServing(), recipeToScale.getTags());
      }
    
    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModifyCommand)) {
            return false;
        }

        // state check
        ModifyCommand e = (ModifyCommand) other;
        return index.equals(e.index)
               && editRecipeDescriptor.equals(e.editRecipeDescriptor);
    }

    /**
     * Stores the details to edit the recipe with. Each non-empty field value will replace the
     * corresponding field value of the recipe.
     */
    public static class EditRecipeDescriptor {
        private Name name;
        private IngredientList ingredients;
        private InstructionList instructions;
        private Calorie calorie;
        private Serving serving;
        private Set<Tag> tags;

        public EditRecipeDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditRecipeDescriptor(EditRecipeDescriptor toCopy) {
            setName(toCopy.name);
            setIngredients(toCopy.ingredients);
            setInstructions(toCopy.instructions);
            setCalorie(toCopy.calorie);
            setServing(toCopy.serving);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, ingredients, instructions, calorie, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public Optional<IngredientList> getIngredients() {
            return Optional.ofNullable(ingredients);
        }

        public void setIngredients(IngredientList ingredients) {
            this.ingredients = ingredients;
        }

        public Optional<InstructionList> getInstructions() {
            return Optional.ofNullable(instructions);
        }

        public void setInstructions(InstructionList instructions) {
            this.instructions = instructions;
        }

        public void setCalorie(Calorie calorie) {
            this.calorie = calorie;
        }

        public Optional<Calorie> getCalorie() {
            return (calorie != null) ? Optional.of(calorie) : Optional.empty();
        }

        public void setServing(Serving serving) {
            this.serving = serving;
        }

        public Optional<Serving> getServing() {
            return (serving != null) ? Optional.of(serving) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditRecipeDescriptor)) {
                return false;
            }

            // state check
            EditRecipeDescriptor e = (EditRecipeDescriptor) other;

            return getName().equals(e.getName())
                    && getIngredients().equals(e.getIngredients())
                    && getInstructions().equals(e.getInstructions())
                    && getCalorie().equals(e.getCalorie())
                    && getServing().equals(e.getServing())
                    && getTags().equals(e.getTags());
        }
    }
}
