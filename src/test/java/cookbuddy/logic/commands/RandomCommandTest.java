package cookbuddy.logic.commands;

import static cookbuddy.testutil.TypicalRecipes.getTypicalRecipeBook;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import cookbuddy.model.Model;
import cookbuddy.model.ModelManager;
import cookbuddy.model.UserPrefs;


/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class RandomCommandTest {

    private Model model = new ModelManager(getTypicalRecipeBook(), new UserPrefs());
    private Model expectedModel;

    @Test
    public void equalityCheck() {
        RandomCommand randomCom = new RandomCommand();
        RandomCommand sameVal = randomCom;
        RandomCommand newRandom = new RandomCommand();
        assertEquals(randomCom, sameVal);
        if (randomCom.getTargetIndex().equals(newRandom.getTargetIndex())) {
            assertEquals(randomCom, newRandom);
        } else {
            assertNotEquals(randomCom, newRandom);
        }
    }

}
