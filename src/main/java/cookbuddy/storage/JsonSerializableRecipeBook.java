package cookbuddy.storage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import cookbuddy.commons.exceptions.IllegalValueException;
import cookbuddy.model.ReadOnlyRecipeBook;
import cookbuddy.model.RecipeBook;
import cookbuddy.model.recipe.Recipe;

/**
 * An Immutable RecipeBook that is serializable to JSON format.
 */
@JsonRootName(value = "recipeBook")
class JsonSerializableRecipeBook {

    public static final String MESSAGE_DUPLICATE_RECIPE = "Recipe list contains duplicate recipe(s).";

    private final List<JsonAdaptedRecipe> recipes = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableRecipeBook} with the given recipes.
     */
    @JsonCreator
    public JsonSerializableRecipeBook(@JsonProperty("recipes") List<JsonAdaptedRecipe> recipes) {
        this.recipes.addAll(recipes);
    }

    /**
     * Converts a given {@code ReadOnlyRecipeBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableRecipeBook}.
     */
    public JsonSerializableRecipeBook(ReadOnlyRecipeBook source) {
        recipes.addAll(source.getRecipeList().stream().map(JsonAdaptedRecipe::new).collect(Collectors.toList()));
    }

    /**
     * Converts this recipe book into the model's {@code RecipeBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public RecipeBook toModelType(Path imagesPath) throws IllegalValueException {
        RecipeBook recipeBook = new RecipeBook();
        for (JsonAdaptedRecipe jsonAdaptedRecipe : recipes) {
            Recipe recipe = jsonAdaptedRecipe.toModelType(imagesPath);
            if (recipeBook.hasRecipe(recipe)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_RECIPE);
            }
            recipeBook.addRecipe(recipe);
        }
        return recipeBook;
    }

}
