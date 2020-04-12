package cookbuddy.logic.commands;

import static cookbuddy.logic.commands.CommandTestUtil.DESC_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.DESC_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_CALORIE_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_DIFFICULTY_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_NAME_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_TAG_BREAKFAST;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cookbuddy.logic.commands.ModifyCommand.EditRecipeDescriptor;
import cookbuddy.testutil.EditRecipeDescriptorBuilder;

public class EditRecipeDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditRecipeDescriptor descriptorWithSameValues = new EditRecipeDescriptor(DESC_HAM_SANDWICH);
        assertTrue(DESC_HAM_SANDWICH.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_HAM_SANDWICH.equals(DESC_HAM_SANDWICH));

        // null -> returns false
        assertFalse(DESC_HAM_SANDWICH.equals(null));

        // different types -> returns false
        assertFalse(DESC_HAM_SANDWICH.equals(5));

        // different values -> returns false
        assertFalse(DESC_HAM_SANDWICH.equals(DESC_EGGS_ON_TOAST));

        // different name -> returns false
        EditRecipeDescriptor editedHamSandwich = new EditRecipeDescriptorBuilder(DESC_HAM_SANDWICH)
            .withName(VALID_NAME_EGGS_ON_TOAST).build();
        assertFalse(DESC_HAM_SANDWICH.equals(editedHamSandwich));

        // different calorie -> returns false
        editedHamSandwich = new EditRecipeDescriptorBuilder(DESC_HAM_SANDWICH).withCalorie(VALID_CALORIE_EGGS_ON_TOAST)
            .build();
        assertFalse(DESC_HAM_SANDWICH.equals(editedHamSandwich));

        // different difficulty -> returns false
        editedHamSandwich = new EditRecipeDescriptorBuilder(DESC_HAM_SANDWICH)
            .withDifficulty(VALID_DIFFICULTY_EGGS_ON_TOAST).build();
        assertFalse(DESC_HAM_SANDWICH.equals(editedHamSandwich));

        // different tags -> returns false
        editedHamSandwich = new EditRecipeDescriptorBuilder(DESC_HAM_SANDWICH).withTags(VALID_TAG_BREAKFAST).build();
        assertFalse(DESC_HAM_SANDWICH.equals(editedHamSandwich));
    }
}
