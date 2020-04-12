package cookbuddy.logic.parser;

import static cookbuddy.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

public class FindCommandParserTest {

    private static final String MESSAGE_ATTRIBUTE_ERROR = "Please search for 1 attribute at a time!";
    private FindCommandParser parser = new FindCommandParser();


    @Test
    public void parse_missingParts_failure() {
        // no prefix specified
        assertParseFailure(parser, "find", MESSAGE_ATTRIBUTE_ERROR);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "i/ string", MESSAGE_ATTRIBUTE_ERROR);
    }

}
