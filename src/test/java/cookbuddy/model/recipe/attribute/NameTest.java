package cookbuddy.model.recipe.attribute;

import static cookbuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cookbuddy.model.recipe.attribute.Name;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Name.isValidName("ham*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(Name.isValidName("ham sandwich")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("ham sandwich 2")); // alphanumeric characters
        assertTrue(Name.isValidName("Ham Sandwich")); // with capital letters
        assertTrue(Name.isValidName("Ham Sandwich with Onions and Lettuce")); // long names
    }
}
