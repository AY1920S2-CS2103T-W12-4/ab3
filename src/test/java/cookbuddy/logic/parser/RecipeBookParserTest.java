package cookbuddy.logic.parser;

import static cookbuddy.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static cookbuddy.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static cookbuddy.testutil.Assert.assertThrows;
import static cookbuddy.testutil.TypicalIndexes.INDEX_FIRST_RECIPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import cookbuddy.logic.commands.DeleteCommand;
import cookbuddy.logic.commands.DoneCommand;
import cookbuddy.logic.commands.ExitCommand;
import cookbuddy.logic.commands.FavCommand;
import cookbuddy.logic.commands.HelpCommand;
import cookbuddy.logic.commands.ListCommand;
import cookbuddy.logic.commands.ResetCommand;
import cookbuddy.logic.commands.UnFavCommand;
import cookbuddy.logic.commands.UndoCommand;
import cookbuddy.logic.commands.ViewCommand;
import cookbuddy.logic.parser.exceptions.ParseException;

public class RecipeBookParserTest {

    private final RecipeBookParser parser = new RecipeBookParser();

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
            DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_RECIPE), command);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), (
        ) -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_reset() throws Exception {
        assertTrue(parser.parseCommand(ResetCommand.COMMAND_WORD) instanceof ResetCommand);
        assertTrue(parser.parseCommand(ResetCommand.COMMAND_WORD) instanceof ResetCommand);
    }


    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
    }

    @Test
    public void parseCommand_fav() throws Exception {
        FavCommand command = (FavCommand) parser.parseCommand(
            FavCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased());
        assertEquals(new FavCommand(INDEX_FIRST_RECIPE), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
    }

    @Test
    public void parseCommand_unfav() throws Exception {
        UnFavCommand command = (UnFavCommand) parser.parseCommand(
            UnFavCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased());
        assertEquals(new UnFavCommand(INDEX_FIRST_RECIPE), command);
    }

    @Test
    public void parseCommand_undone() throws Exception {
        UndoCommand command = (UndoCommand) parser.parseCommand(
            UndoCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased());
        assertEquals(new UndoCommand(INDEX_FIRST_RECIPE), command);
    }

    @Test
    public void parseCommand_view() throws Exception {
        ViewCommand command = (ViewCommand) parser.parseCommand(
            ViewCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased());
        assertEquals(new ViewCommand(INDEX_FIRST_RECIPE), command);
    }

    @Test
    public void parseCommand_done() throws Exception {
        DoneCommand command = (DoneCommand) parser.parseCommand(
            DoneCommand.COMMAND_WORD + " " + INDEX_FIRST_RECIPE.getOneBased());
        assertEquals(new DoneCommand(INDEX_FIRST_RECIPE), command);
    }


}

