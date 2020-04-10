package cookbuddy.logic.commands;

import static cookbuddy.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static cookbuddy.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import cookbuddy.logic.parser.ParserUtil;
import cookbuddy.logic.parser.exceptions.ParseException;
import cookbuddy.model.recipe.attribute.Name;
import cookbuddy.model.recipe.attribute.Rating;
import cookbuddy.model.recipe.attribute.Tag;
import cookbuddy.model.recipe.attribute.Time;




/**
 * Contains helper methods for testing commands.
 */
public class ParserTestUtil {
    private static final String INVALID_NAME = "";
    private static final String INVALID_INGREDIENTS = "";
    private static final String INVALID_INSTRUCTIONS = "";
    private static final String INVALID_RATING = "12";
    private static final String INVALID_SERVING = "-1";
    private static final String INVALID_CALORIE = "-100";
    private static final String INVALID_DIFFICULTY = "7";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_TIME = "-1:-1:-1";

    //TODO: Remove fields referencing person.
    private static final String VALID_NAME = "Special Sandwich";
    private static final String VALID_INGREDIENTS = "ham, 2slices; cheese, 1 slice; bread, 2 slices";
    private static final String VALID_INSTRUCTIONS = "add ham to bread; add cheese; eat";
    private static final String VALID_RATING = "5";
    private static final String VALID_SERVING = "2";
    private static final String VALID_CALORIE = "1000";
    private static final String VALID_DIFFICULTY = "2";
    private static final String VALID_TAG_1 = "easy";
    private static final String VALID_TAG_2 = "snack";
    private static final String VALID_TIME = "12:00:00";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    /*
    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }*/

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }
    @Test
    public void parseRating_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseRating(INVALID_RATING));
    }
    @Test
    public void parseDifficulty_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDifficulty(INVALID_DIFFICULTY));
    }

    @Test
    public void parseTiming_invalidFormat_throwsNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> ParserUtil.parseTime("abc"));
    }

    @Test
    public void parseTiming_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTime(INVALID_TIME));
    }

    @Test
    public void parseTiming_validValue_returnsTime() throws Exception {
        Time time = new Time(12, 00, 00);
        assertEquals(time, (ParserUtil.parseTime(VALID_TIME)));
    }

    @Test
    public void parseRating_validValue_returnsRating() throws Exception {
        Rating testRating = new Rating(5);
        assertEquals(testRating, (ParserUtil.parseRating(VALID_RATING)));
    }

    @Test
    public void parseRating_invalidType_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseRating("abc"));
    }



    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Optional.of((VALID_TAG_1 + INVALID_TAG))));
    }

    /*
    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }*/

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Optional.of(VALID_TAG_1 + VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1 + VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }
}
