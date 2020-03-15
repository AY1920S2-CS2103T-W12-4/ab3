package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.recipe.IngredientList;
import seedu.address.model.recipe.InstructionList;
import seedu.address.model.recipe.Name;
import seedu.address.model.recipe.Recipe;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Recipe}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Recipe's %s field is missing!";

    private final String name;
    private final String ingredients;
    private final String instructions;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given recipe details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("ingredients") String ingredients,
                             @JsonProperty("instructions") String instructions,
                             @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Recipe} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Recipe source) {
        name = source.getName().name;
        ingredients = source.getIngredients().ingredientListString;
        instructions = source.getInstructions().instructionListString;
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted recipe object into the model's {@code Recipe} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted recipe.
     */
    public Recipe toModelType() throws IllegalValueException {
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
        if (!IngredientList.isValidIngredients(ingredients)) {
            throw new IllegalValueException(IngredientList.MESSAGE_CONSTRAINTS);
        }
        final IngredientList modelIngredients = new IngredientList(ingredients);

        if (instructions == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, InstructionList.class.getSimpleName()));
        }
        if (!InstructionList.isValidInstructions(instructions)) {
            throw new IllegalValueException(InstructionList.MESSAGE_CONSTRAINTS);
        }
        final InstructionList modelInstructions = new InstructionList(instructions);

        final Set<Tag> modelTags = new HashSet<>(recipeTags);
        return new Recipe(modelName, modelIngredients, modelInstructions, modelTags);
    }

}
