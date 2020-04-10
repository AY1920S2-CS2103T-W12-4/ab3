package cookbuddy.logic.commands;

import static cookbuddy.logic.parser.CliSyntax.PREFIX_CALORIE;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_DIFFICULTY;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_IMAGEFILEPATH;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_INGREDIENTS;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_INSTRUCTIONS;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_NAME;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_RATING;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_SERVING;
import static cookbuddy.logic.parser.CliSyntax.PREFIX_TAG;
import static cookbuddy.model.Model.PREDICATE_SHOW_ALL_RECIPES;
import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import cookbuddy.commons.core.Messages;
import cookbuddy.commons.core.index.Index;
import cookbuddy.commons.util.CollectionUtil;
import cookbuddy.logic.commands.exceptions.CommandException;
import cookbuddy.model.Model;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.model.recipe.attribute.Calorie;
import cookbuddy.model.recipe.attribute.Difficulty;
import cookbuddy.model.recipe.attribute.IngredientList;
import cookbuddy.model.recipe.attribute.InstructionList;
import cookbuddy.model.recipe.attribute.Name;
import cookbuddy.model.recipe.attribute.Photograph;
import cookbuddy.model.recipe.attribute.Rating;
import cookbuddy.model.recipe.attribute.Serving;
import cookbuddy.model.recipe.attribute.Tag;
import cookbuddy.ui.UiManager;

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
        + "[" + PREFIX_INGREDIENTS + "INGREDIENT 1, QUANTITY; INGREDIENT 2, QUANTITY] "
        + "[" + PREFIX_INSTRUCTIONS + "INSTRUCTION 1; INSTRUCTION 2] "
        + "[" + PREFIX_IMAGEFILEPATH + "PATH] "
        + "[" + PREFIX_CALORIE + "CALORIES] "
        + "[" + PREFIX_SERVING + "SERVING] "
        + "[" + PREFIX_RATING + "RATING] "
        + "[" + PREFIX_DIFFICULTY + "DIFFICULTY] "
        + "[" + PREFIX_TAG + "TAG [TAG]...]\n"
        + "Example: " + COMMAND_WORD + " 1 "
        + PREFIX_INGREDIENTS + "toast, 2; eggs, 1 "
        + PREFIX_INSTRUCTIONS + "put egg on toast; put bread on egg";

    public static final String MESSAGE_EDIT_RECIPE_SUCCESS = "Edited Recipe: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_RECIPE = "This recipe already exists in the recipe book.";

    private final Index index;
    private final EditRecipeDescriptor editRecipeDescriptor;

    /**
     * @param index                of the recipe in the filtered recipe list to edit
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
        if (UiManager.getViewedRecipe() == recipeToEdit) {
            UiManager.changeRecipe(editedRecipe);
        }
        return new CommandResult(String.format(MESSAGE_EDIT_RECIPE_SUCCESS, editedRecipe));
    }

    /**
     * Creates and returns a {@code Recipe} with the details of {@code recipeToEdit}
     * edited with {@code editRecipeDescriptor}.
     */
    public static Recipe createEditedRecipe(Recipe recipeToEdit, EditRecipeDescriptor editRecipeDescriptor) {
        assert recipeToEdit != null;

        Name updatedName = editRecipeDescriptor.getName().orElse(recipeToEdit.getName());
        IngredientList updatedIngredients = editRecipeDescriptor.getIngredients().orElse(recipeToEdit.getIngredients());
        InstructionList updatedInstructions =
            editRecipeDescriptor.getInstructions().orElse(recipeToEdit.getInstructions());
        Photograph updatedImage = editRecipeDescriptor.getImageFilePath().orElse(recipeToEdit.getPhotograph());
        Calorie updatedCalorie = editRecipeDescriptor.getCalorie().orElse(recipeToEdit.getCalorie());
        Serving updatedServing = editRecipeDescriptor.getServing().orElse(recipeToEdit.getServing());
        Rating updatedRating = editRecipeDescriptor.getRating().orElse(recipeToEdit.getRating());
        Difficulty updatedDifficulty = editRecipeDescriptor.getDifficulty().orElse(recipeToEdit.getDifficulty());
        Set<Tag> updatedTags = editRecipeDescriptor.getTags().orElse(recipeToEdit.getTags());

        return new Recipe(updatedName, updatedIngredients, updatedInstructions, updatedImage, updatedCalorie,
            updatedServing,
            updatedRating, updatedDifficulty, updatedTags);
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
        private Photograph imageFilePath;
        private Calorie calorie;
        private Serving serving;
        private Rating rating;
        private Difficulty difficulty;
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
            setImageFilePath(toCopy.imageFilePath);
            setCalorie(toCopy.calorie);
            setServing(toCopy.serving);
            setRating(toCopy.rating);
            setDifficulty(toCopy.difficulty);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, ingredients, instructions, imageFilePath, calorie, serving, rating,
                difficulty, tags);
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

        public Optional<Photograph> getImageFilePath() {
            return Optional.ofNullable(imageFilePath);
        }

        public void setImageFilePath(Photograph imageFilePath) {
            this.imageFilePath = imageFilePath;
        }

        public void setCalorie(Calorie calorie) {
            this.calorie = calorie;
        }

        public Optional<Calorie> getCalorie() {
            return (calorie != null)
                ? Optional.of(calorie)
                : Optional.empty();
        }

        public void setServing(Serving serving) {
            this.serving = serving;
        }

        public Optional<Serving> getServing() {
            return (serving != null)
                ? Optional.of(serving)
                : Optional.empty();
        }

        public void setRating(Rating rating) {
            this.rating = rating;
        }

        public Optional<Rating> getRating() {
            return (rating != null)
                ? Optional.of(rating)
                : Optional.empty();
        }


        public void setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
        }

        public Optional<Difficulty> getDifficulty() {
            return (difficulty != null) ? Optional.of(difficulty) : Optional.empty();
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null)
                ? new HashSet<>(tags)
                : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null)
                ? Optional.of(Collections.unmodifiableSet(tags))
                : Optional.empty();
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
                //  && getImageFilePath().equals(e.getImageFilePath())
                && getCalorie().equals(e.getCalorie())
                && getServing().equals(e.getServing())
                && getRating().equals(e.getRating())
                && getDifficulty().equals(e.getDifficulty())
                && getTags().equals(e.getTags());
        }
    }
}
