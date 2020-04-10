package cookbuddy.testutil;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cookbuddy.logic.commands.ModifyCommand;
import cookbuddy.logic.commands.ModifyCommand.EditRecipeDescriptor;
import cookbuddy.model.recipe.Recipe;
import cookbuddy.model.recipe.attribute.Calorie;
import cookbuddy.model.recipe.attribute.Difficulty;
import cookbuddy.model.recipe.attribute.Ingredient;
import cookbuddy.model.recipe.attribute.IngredientList;
import cookbuddy.model.recipe.attribute.Instruction;
import cookbuddy.model.recipe.attribute.InstructionList;
import cookbuddy.model.recipe.attribute.Name;
import cookbuddy.model.recipe.attribute.Photograph;
import cookbuddy.model.recipe.attribute.Rating;
import cookbuddy.model.recipe.attribute.Serving;
import cookbuddy.model.recipe.attribute.Tag;

/**
 * A utility class to help with building EditRecipeDescriptor objects.
 */
public class EditRecipeDescriptorBuilder {

    private ModifyCommand.EditRecipeDescriptor descriptor;

    public EditRecipeDescriptorBuilder() {
        descriptor = new ModifyCommand.EditRecipeDescriptor();
    }

    public EditRecipeDescriptorBuilder(ModifyCommand.EditRecipeDescriptor descriptor) {
        this.descriptor = new EditRecipeDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditRecipeDescriptor} with fields containing {@code recipe}'s details
     */
    public EditRecipeDescriptorBuilder(Recipe recipe) {
        descriptor = new EditRecipeDescriptor();
        descriptor.setName(recipe.getName());
        descriptor.setIngredients(recipe.getIngredients());
        descriptor.setInstructions(recipe.getInstructions());
        descriptor.setImageFilePath(recipe.getPhotograph());
        descriptor.setCalorie(recipe.getCalorie());
        descriptor.setServing(recipe.getServing());
        descriptor.setRating(recipe.getRating());
        descriptor.setDifficulty(recipe.getDifficulty());
        descriptor.setTags(recipe.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code IngredientList} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withIngredients(String ingredients) {
        List<Ingredient> ingredientList =
                Stream.of(ingredients.trim().split(";")).map(String::trim).map(Ingredient::new)
                        .collect(Collectors.toList());
        descriptor.setIngredients(new IngredientList(ingredientList));
        return this;
    }

    /**
     * Sets the {@code InstructionList} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withInstructions(String instructions) {
        List<Instruction> instructionList = (Stream.of(instructions.trim().split(";")).map(String::trim)
                .map(Instruction::new).collect(Collectors.toList()));
        descriptor.setInstructions(new InstructionList(instructionList));
        return this;
    }

    /**
     * Sets the {@code Photograph} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withPhotograph(String photograph) {
        Photograph ph;
        try {
            ph = new Photograph(photograph);
        } catch (IOException e) {
            return this;
        }
        descriptor.setImageFilePath(ph);
        return this;
    }

    /**
     * Sets the {@code Calorie} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withCalorie(String calorie) {
        descriptor.setCalorie(new Calorie(calorie));
        return this;
    }

    /**
     * Sets the {@code Serving} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withServing(int serving) {
        descriptor.setServing(new Serving(serving));
        return this;
    }

    /**
     * Sets the {@code Rating} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withRating(int rating) {
        descriptor.setRating(new Rating(rating));
        return this;
    }

    /**
     * Sets the {@code Difficulty} of the {@code EditRecipeDescriptor} that we are building.
     */
    public EditRecipeDescriptorBuilder withDifficulty(int difficulty) {
        descriptor.setDifficulty(new Difficulty(difficulty));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditRecipeDescriptor}
     * that we are building.
     */
    public EditRecipeDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }


    public ModifyCommand.EditRecipeDescriptor build() {
        return descriptor;
    }
}
