package cookbuddy.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import cookbuddy.commons.exceptions.IllegalValueException;
import cookbuddy.commons.util.FileUtil;
import cookbuddy.logic.parser.ParserUtil;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.model.recipe.testattribute.Calorie;
import cookbuddy.model.recipe.testattribute.Difficulty;
import cookbuddy.model.recipe.testattribute.IngredientList;
import cookbuddy.model.recipe.testattribute.InstructionList;
import cookbuddy.model.recipe.testattribute.Name;
import cookbuddy.model.recipe.testattribute.Photograph;
import cookbuddy.model.recipe.testattribute.Rating;
import cookbuddy.model.recipe.testattribute.Serving;
import cookbuddy.model.recipe.testattribute.Tag;

/**
 * Jackson-friendly version of {@link Recipe}.
 */
class JsonAdaptedRecipe {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Recipe's %s field is missing!";

    private final String name;
    private final String ingredients;
    private final String instructions;
    private final Path imageFilePath;
    private final String calorie;
    private final int serving;
    private final int rating;
    private final int difficulty;
    private final String fav;
    private final String done;
    private final String time;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedRecipe} with the given recipe details.
     */
    @JsonCreator
    public JsonAdaptedRecipe(@JsonProperty("name") String name, @JsonProperty("ingredients") String ingredients,
            @JsonProperty("instructions") String instructions, @JsonProperty("filePath") Path imageFilePath,
            @JsonProperty("calorie") String calorie, @JsonProperty("serving") int serving,
            @JsonProperty("rating") int rating, @JsonProperty("difficulty") int difficulty,
            @JsonProperty("fav") String fav, @JsonProperty("done") String done, @JsonProperty("time") String time,
            @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.imageFilePath = imageFilePath;
        this.calorie = calorie;
        this.serving = serving;
        this.rating = rating;
        this.fav = fav;
        this.done = done;
        this.time = time;
        this.difficulty = difficulty;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Recipe} into this class for Jackson use.
     */
    public JsonAdaptedRecipe(Recipe source) {
        name = source.getName().toString();
        ingredients = source.getIngredients().toString();
        instructions = source.getInstructions().toString();
        imageFilePath = source.getPhotograph().getImageFileName(source);
        calorie = source.getCalorie().calorie;
        serving = source.getServing().serving;
        rating = source.getRating().rating;
        difficulty = source.getDifficulty().difficulty;
        fav = source.getFavStatus().toString();
        done = source.getDoneStatus().toString();
        time = source.getPrepTime().toString();
        tagged.addAll(source.getTags().stream().map(JsonAdaptedTag::new).collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted recipe object into the model's
     * {@code Recipe} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted recipe.
     */
    public Recipe toModelType(Path imageStoragePath) throws IllegalValueException {
        final List<Tag> recipeTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            recipeTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (ingredients == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, IngredientList.class.getSimpleName()));
        }
        // if (!IngredientList.isValidIngredients(ingredients)) {
        // throw new IllegalValueException(IngredientList.MESSAGE_CONSTRAINTS);
        // }
        final IngredientList modelIngredients = ParserUtil.parseIngredients(ingredients);

        if (instructions == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, InstructionList.class.getSimpleName()));
        }
        // if (!InstructionList.isValidInstructions(instructions)) {
        // throw new IllegalValueException(InstructionList.MESSAGE_CONSTRAINTS);
        // }
        final InstructionList modelInstructions = ParserUtil.parseInstructions(instructions);

        if (imageFilePath == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Photograph.class.getSimpleName()));
        }

        if (!FileUtil.isValidPathString(imageFilePath.toString())) {
            throw new IllegalValueException(Photograph.MESSAGE_CONSTRAINTS);
        }

        Photograph modelPhotograph;
        try {
            modelPhotograph = Photograph.IMAGE_UTIL.isPlaceHolderImage(
                    FileUtil.joinPaths(imageStoragePath, imageFilePath)) ? Photograph.PLACEHOLDER_PHOTOGRAPH
                            : new Photograph(FileUtil.joinPaths(imageStoragePath, imageFilePath));
        } catch (IOException e) {
            modelPhotograph = Photograph.PLACEHOLDER_PHOTOGRAPH;
        }

        final Calorie modelCalorie = new Calorie(calorie);
        final Serving modelServe = new Serving(serving);
        final Rating modelRating = new Rating(rating);
        final Difficulty modelDifficulty = new Difficulty(difficulty);
        final Set<Tag> modelTags = new HashSet<>(recipeTags);

        Recipe toReturn = new Recipe(modelName, modelIngredients, modelInstructions, modelPhotograph, modelCalorie,
                modelServe, modelRating, modelDifficulty, modelTags);

        if (fav.equals("\u2665")) {
            toReturn.favRecipe();
        }

        if (done.equals("Yes")) {
            toReturn.attemptRecipe();
        }

        if (!time.equals("-")) {
            toReturn.setTime(ParserUtil.parseTime(time));
        }

        return toReturn;
    }
}
