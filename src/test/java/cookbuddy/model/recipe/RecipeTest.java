package cookbuddy.model.recipe;

import static cookbuddy.logic.commands.CommandTestUtil.VALID_HOUR_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_INGREDIENTS_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_INSTRUCTIONS_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_MIN_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_NAME_EGGS_ON_TOAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_SEC_HAM_SANDWICH;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_TAG_BREAKFAST;
import static cookbuddy.logic.commands.CommandTestUtil.VALID_TAG_LUNCH;
import static cookbuddy.testutil.Assert.assertThrows;
import static cookbuddy.testutil.TypicalRecipes.EGGS_ON_TOAST;
import static cookbuddy.testutil.TypicalRecipes.HAM_SANDWICH;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cookbuddy.model.recipe.attribute.Calorie;
import cookbuddy.testutil.RecipeBuilder;


public class RecipeTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Recipe recipe = new RecipeBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> recipe.getTags().remove(0));
    }

    @Test
    public void isSameRecipe() {
        // same object -> returns true
        assertTrue(HAM_SANDWICH.isSameRecipe(HAM_SANDWICH));

        // null -> returns false
        assertFalse(HAM_SANDWICH.isSameRecipe(null));

        // different phone and email -> returns false
        Recipe editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withIngredients(VALID_INGREDIENTS_EGGS_ON_TOAST)
                .withInstructions(VALID_INSTRUCTIONS_EGGS_ON_TOAST).build();
        assertFalse(HAM_SANDWICH.isSameRecipe(editedHamSandwich));

        // different name -> returns false
        editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withName(VALID_NAME_EGGS_ON_TOAST).build();
        assertFalse(HAM_SANDWICH.isSameRecipe(editedHamSandwich));

        // same name, same instructions, different attributes -> returns false
        editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withIngredients(VALID_INGREDIENTS_EGGS_ON_TOAST).withTags(
                VALID_TAG_LUNCH).build();
        assertFalse(HAM_SANDWICH.isSameRecipe(editedHamSandwich));

        // same name, same ingredients, different attributes -> returns false
        editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withInstructions(VALID_INSTRUCTIONS_EGGS_ON_TOAST).withTags(
                VALID_TAG_BREAKFAST).build();
        assertFalse(HAM_SANDWICH.isSameRecipe(editedHamSandwich));

        editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).addTime(VALID_HOUR_HAM_SANDWICH,
                VALID_MIN_HAM_SANDWICH, VALID_SEC_HAM_SANDWICH).build();
        assertTrue(HAM_SANDWICH.isSameRecipe(editedHamSandwich));



        // same name, same ingredients, same instructions, different attributes -> returns true
        editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withTags(VALID_TAG_BREAKFAST).build();
        assertTrue(HAM_SANDWICH.isSameRecipe(editedHamSandwich));
    }

    @Test
    public void sameCalorieStringChecker() {
        assertTrue(HAM_SANDWICH.getCalorie().toString().equals(HAM_SANDWICH.getCalorie().toString()));
    }

    @Test
    public void equalCalorieStringChecker() {
        assertTrue(HAM_SANDWICH.getCalorie().equals(new Calorie("300")));
    }

    @Test
    public void equalCalorieHashChecker() {
        assertTrue(new Calorie("200").hashCode() == new Calorie("200").hashCode());
    }

    @Test
    public void unEqualCalorieStringChecker() {
        assertFalse(HAM_SANDWICH.getCalorie().toString().equals(new Calorie("200").toString()));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Recipe hamSandwichBuilder = new RecipeBuilder(HAM_SANDWICH).build();
        assertTrue(HAM_SANDWICH.equals(hamSandwichBuilder));

        // same object -> returns true
        assertTrue(HAM_SANDWICH.equals(HAM_SANDWICH));

        // null -> returns false
        assertFalse(HAM_SANDWICH.equals(null));

        // different type -> returns false
        assertFalse(HAM_SANDWICH.equals(5));

        // different recipe -> returns false
        assertFalse(HAM_SANDWICH.equals(EGGS_ON_TOAST));

        // different name -> returns false
        Recipe editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withName(VALID_NAME_EGGS_ON_TOAST).build();
        assertFalse(HAM_SANDWICH.equals(editedHamSandwich));

        // TODO: Check what happens when Recipes with same name have different ingredients and/or instructions

        // different tags -> returns false
        editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withTags(VALID_TAG_BREAKFAST).build();
        assertFalse(HAM_SANDWICH.equals(editedHamSandwich));
    }
}
