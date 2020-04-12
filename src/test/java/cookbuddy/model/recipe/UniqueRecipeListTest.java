package cookbuddy.model.recipe;

import static cookbuddy.logic.commands.CommandTestUtil.VALID_TAG_BREAKFAST;
import static cookbuddy.testutil.Assert.assertThrows;
import static cookbuddy.testutil.TypicalRecipes.EGGS_ON_TOAST;
import static cookbuddy.testutil.TypicalRecipes.HAM_SANDWICH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import cookbuddy.model.recipe.exceptions.DuplicateRecipeException;
import cookbuddy.model.recipe.exceptions.RecipeNotFoundException;
import cookbuddy.testutil.RecipeBuilder;

public class UniqueRecipeListTest {

    private final UniqueRecipeList uniqueRecipeList = new UniqueRecipeList();

    @Test
    public void contains_nullRecipe_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRecipeList.contains(null));
    }

    @Test
    public void contains_recipeNotInList_returnsFalse() {
        assertFalse(uniqueRecipeList.contains(HAM_SANDWICH));
    }

    @Test
    public void contains_recipeInList_returnsTrue() {
        uniqueRecipeList.add(HAM_SANDWICH);
        assertTrue(uniqueRecipeList.contains(HAM_SANDWICH));
    }

    @Test
    public void contains_recipeWithSameIdentityFieldsInList_returnsTrue() {
        uniqueRecipeList.add(HAM_SANDWICH);
        Recipe editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withTags(VALID_TAG_BREAKFAST)
            .build();
        assertTrue(uniqueRecipeList.contains(editedHamSandwich));
    }

    @Test
    public void add_nullRecipe_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRecipeList.add(null));
    }

    @Test
    public void add_duplicateRecipe_throwsDuplicateRecipeException() {
        uniqueRecipeList.add(HAM_SANDWICH);
        assertThrows(DuplicateRecipeException.class, () -> uniqueRecipeList.add(HAM_SANDWICH));
    }

    @Test
    public void setRecipe_nullTargetRecipe_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRecipeList.setRecipe(null, HAM_SANDWICH));
    }

    @Test
    public void setRecipe_nullEditedRecipe_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRecipeList.setRecipe(HAM_SANDWICH, null));
    }

    @Test
    public void setRecipe_targetRecipeNotInList_throwsRecipeNotFoundException() {
        assertThrows(RecipeNotFoundException.class, () -> uniqueRecipeList.setRecipe(HAM_SANDWICH, HAM_SANDWICH));
    }

    @Test
    public void setRecipe_editedRecipeIsSameRecipe_success() {
        uniqueRecipeList.add(HAM_SANDWICH);
        uniqueRecipeList.setRecipe(HAM_SANDWICH, HAM_SANDWICH);
        UniqueRecipeList expectedUniqueRecipeList = new UniqueRecipeList();
        expectedUniqueRecipeList.add(HAM_SANDWICH);
        assertEquals(expectedUniqueRecipeList, uniqueRecipeList);
    }

    @Test
    public void setRecipe_editedRecipeHasSameIdentity_success() {
        uniqueRecipeList.add(HAM_SANDWICH);
        Recipe editedHamSandwich = new RecipeBuilder(HAM_SANDWICH).withTags(VALID_TAG_BREAKFAST)
            .build();
        uniqueRecipeList.setRecipe(HAM_SANDWICH, editedHamSandwich);
        UniqueRecipeList expectedUniqueRecipeList = new UniqueRecipeList();
        expectedUniqueRecipeList.add(editedHamSandwich);
        assertEquals(expectedUniqueRecipeList, uniqueRecipeList);
    }

    @Test
    public void setRecipe_editedRecipeHasDifferentIdentity_success() {
        uniqueRecipeList.add(HAM_SANDWICH);
        uniqueRecipeList.setRecipe(HAM_SANDWICH, EGGS_ON_TOAST);
        UniqueRecipeList expectedUniqueRecipeList = new UniqueRecipeList();
        expectedUniqueRecipeList.add(EGGS_ON_TOAST);
        assertEquals(expectedUniqueRecipeList, uniqueRecipeList);
    }

    @Test
    public void setRecipe_editedRecipeHasNonUniqueIdentity_throwsDuplicateRecipeException() {
        uniqueRecipeList.add(HAM_SANDWICH);
        uniqueRecipeList.add(EGGS_ON_TOAST);
        assertThrows(DuplicateRecipeException.class, () -> uniqueRecipeList.setRecipe(HAM_SANDWICH, EGGS_ON_TOAST));
    }

    @Test
    public void remove_nullRecipe_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRecipeList.remove(null));
    }

    @Test
    public void remove_recipeDoesNotExist_throwsRecipeNotFoundException() {
        assertThrows(RecipeNotFoundException.class, () -> uniqueRecipeList.remove(HAM_SANDWICH));
    }

    @Test
    public void remove_existingRecipe_removesRecipe() {
        uniqueRecipeList.add(HAM_SANDWICH);
        uniqueRecipeList.remove(HAM_SANDWICH);
        UniqueRecipeList expectedUniqueRecipeList = new UniqueRecipeList();
        assertEquals(expectedUniqueRecipeList, uniqueRecipeList);
    }

    @Test
    public void setRecipes_nullUniqueRecipeList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRecipeList.setRecipes((UniqueRecipeList) null));
    }

    @Test
    public void setRecipes_uniqueRecipeList_replacesOwnListWithProvidedUniqueRecipeList() {
        uniqueRecipeList.add(HAM_SANDWICH);
        UniqueRecipeList expectedUniqueRecipeList = new UniqueRecipeList();
        expectedUniqueRecipeList.add(EGGS_ON_TOAST);
        uniqueRecipeList.setRecipes(expectedUniqueRecipeList);
        assertEquals(expectedUniqueRecipeList, uniqueRecipeList);
    }

    @Test
    public void setRecipes_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueRecipeList.setRecipes((List<Recipe>) null));
    }

    @Test
    public void setRecipes_list_replacesOwnListWithProvidedList() {
        uniqueRecipeList.add(HAM_SANDWICH);
        List<Recipe> recipeList = Collections.singletonList(EGGS_ON_TOAST);
        uniqueRecipeList.setRecipes(recipeList);
        UniqueRecipeList expectedUniqueRecipeList = new UniqueRecipeList();
        expectedUniqueRecipeList.add(EGGS_ON_TOAST);
        assertEquals(expectedUniqueRecipeList, uniqueRecipeList);
    }

    @Test
    public void setRecipes_listWithDuplicateRecipes_throwsDuplicateRecipeException() {
        List<Recipe> listWithDuplicateRecipes = Arrays.asList(HAM_SANDWICH, HAM_SANDWICH);
        assertThrows(DuplicateRecipeException.class, () -> uniqueRecipeList.setRecipes(listWithDuplicateRecipes));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueRecipeList.asUnmodifiableObservableList().remove(0));
    }
}
