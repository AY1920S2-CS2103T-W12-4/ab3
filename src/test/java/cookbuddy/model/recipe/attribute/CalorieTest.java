package cookbuddy.model.recipe.attribute;

import static cookbuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CalorieTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Calorie(null));
    }

    @Test
    public void constructor_invalidCalorie_throwsIllegalArgumentException() {
        String invalidCalorie = "";
        assertThrows(IllegalArgumentException.class, () -> new Calorie(invalidCalorie));
    }

    @Test
    public void isValidCalorieAmount() {
        // null calorie
        assertThrows(NullPointerException.class, () -> Calorie.isValidCalorieAmount(null));

        // invalid calorie
        assertFalse(Calorie.isValidCalorieAmount("")); // empty string
        assertFalse(Calorie.isValidCalorieAmount(" ")); // spaces only
        assertFalse(Calorie.isValidCalorieAmount("ham*")); // contains non-numeric characters
        assertFalse(Calorie.isValidCalorieAmount("-3")); // contains negative numeric characters

        // valid calorie
        assertTrue(Calorie.isValidCalorieAmount("120")); // numbers only
        assertTrue(Calorie.isValidCalorieAmount("123456")); // large numbers
    }

}
