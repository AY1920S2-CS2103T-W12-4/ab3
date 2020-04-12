package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ModifyCommand.EditRecipeDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.RecipeBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.recipe.Recipe;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * ModifyCommand.
 */
public class ModifyCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Recipe editedRecipe = new PersonBuilder().build();
        ModifyCommand.EditRecipeDescriptor descriptor = new EditPersonDescriptorBuilder(editedRecipe).build();
        ModifyCommand modifyCommand = new ModifyCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(ModifyCommand.MESSAGE_EDIT_RECIPE_SUCCESS, editedRecipe);

        Model expectedModel = new ModelManager(new RecipeBook(model.getRecipeBook()), new UserPrefs());
        expectedModel.setRecipe(model.getFilteredRecipeList().get(0), editedRecipe);

        assertCommandSuccess(modifyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredRecipeList().size());
        Recipe lastRecipe = model.getFilteredRecipeList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastRecipe);
        Recipe editedRecipe = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB).withTags(
                VALID_TAG_HUSBAND).build();

        EditRecipeDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).withTags(
                VALID_TAG_HUSBAND).build();
        ModifyCommand modifyCommand = new ModifyCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(ModifyCommand.MESSAGE_EDIT_RECIPE_SUCCESS, editedRecipe);

        Model expectedModel = new ModelManager(new RecipeBook(model.getRecipeBook()), new UserPrefs());
        expectedModel.setRecipe(lastRecipe, editedRecipe);

        assertCommandSuccess(modifyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        ModifyCommand modifyCommand = new ModifyCommand(INDEX_FIRST_PERSON, new EditRecipeDescriptor());
        Recipe editedRecipe = model.getFilteredRecipeList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(ModifyCommand.MESSAGE_EDIT_RECIPE_SUCCESS, editedRecipe);

        Model expectedModel = new ModelManager(new RecipeBook(model.getRecipeBook()), new UserPrefs());

        assertCommandSuccess(modifyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Recipe recipeInFilteredList = model.getFilteredRecipeList().get(INDEX_FIRST_PERSON.getZeroBased());
        Recipe editedRecipe = new PersonBuilder(recipeInFilteredList).withName(VALID_NAME_BOB).build();
        ModifyCommand modifyCommand = new ModifyCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(ModifyCommand.MESSAGE_EDIT_RECIPE_SUCCESS, editedRecipe);

        Model expectedModel = new ModelManager(new RecipeBook(model.getRecipeBook()), new UserPrefs());
        expectedModel.setRecipe(model.getFilteredRecipeList().get(0), editedRecipe);

        assertCommandSuccess(modifyCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Recipe firstRecipe = model.getFilteredRecipeList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditRecipeDescriptor descriptor = new EditPersonDescriptorBuilder(firstRecipe).build();
        ModifyCommand modifyCommand = new ModifyCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(modifyCommand, model, ModifyCommand.MESSAGE_DUPLICATE_RECIPE);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit recipe in filtered list into a duplicate in address book
        Recipe recipeInList = model.getRecipeBook().getRecipeList().get(INDEX_SECOND_PERSON.getZeroBased());
        ModifyCommand modifyCommand = new ModifyCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(recipeInList).build());

        assertCommandFailure(modifyCommand, model, ModifyCommand.MESSAGE_DUPLICATE_RECIPE);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredRecipeList().size() + 1);
        EditRecipeDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        ModifyCommand modifyCommand = new ModifyCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(modifyCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getRecipeBook().getRecipeList().size());

        ModifyCommand modifyCommand = new ModifyCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(modifyCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final ModifyCommand standardCommand = new ModifyCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        ModifyCommand.EditRecipeDescriptor copyDescriptor = new ModifyCommand.EditRecipeDescriptor(DESC_AMY);
        ModifyCommand commandWithSameValues = new ModifyCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ResetCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new ModifyCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new ModifyCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

}
