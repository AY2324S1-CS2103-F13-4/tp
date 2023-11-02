package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EVENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddContactEventCommand;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.EditContactEventCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Event;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditEventDescriptorBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.EventBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class UniMateParserTest {

    private final UniMateParser parser = new UniMateParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_editContactEvent() throws Exception {
        Person person = new PersonBuilder().withCalendar().build();
        Event event = person.getCalendar().getEventList().get(INDEX_FIRST_EVENT.getOneBased());
        EditContactEventCommand.EditEventDescriptor descriptor = new EditEventDescriptorBuilder(event).build();
        EditContactEventCommand command = (EditContactEventCommand)
                parser.parseCommand(EditContactEventCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + INDEX_FIRST_EVENT.getOneBased() + " "
                        + PersonUtil.getEditPersonEventDetails(descriptor));
        ArrayList<Index> expectedIndexArray = new ArrayList<>();
        expectedIndexArray.add(INDEX_FIRST_PERSON);
        expectedIndexArray.add(INDEX_FIRST_EVENT);
        assertEquals(new EditContactEventCommand(expectedIndexArray, descriptor), command);
    }

    @Test
    public void parseCommand_editContactEvent2() throws Exception {
        Person person = new PersonBuilder().withCalendar().build();
        EditContactEventCommand.EditEventDescriptor descriptor = new EditContactEventCommand.EditEventDescriptor();
        EditContactEventCommand command = (EditContactEventCommand)
                parser.parseCommand(EditContactEventCommand.COMMAND_WORD + " "
                        + INDEX_FIRST_PERSON.getOneBased() + " " + INDEX_FIRST_EVENT.getOneBased()
                        + " d/Sleep");
        assertTrue(command instanceof EditContactEventCommand);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_addEvent() throws Exception {
        String description = "sleep";
        String startDateTime = "2023-10-10 10:00";
        String endDateTime = "2023-10-10 12:00";
        String validArg = "d/" + description
                + " ts/" + startDateTime
                + " te/" + endDateTime;
        validArg = AddEventCommand.COMMAND_WORD + " " + validArg;
        EventBuilder eventBuilder = new EventBuilder();
        eventBuilder.withDescription(description);
        eventBuilder.withStartEndDate(startDateTime, endDateTime);

        AddEventCommand addEventCommand = (AddEventCommand) parser.parseCommand(validArg);
        assertEquals(addEventCommand, new AddEventCommand(eventBuilder.build()));
    }

    @Test
    public void parseCommand_sort() throws Exception {
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " "
                + SortCommand.SORTBY_KEYWORD1) instanceof SortCommand);
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " "
                + SortCommand.SORTBY_KEYWORD2) instanceof SortCommand);
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " "
                + SortCommand.SORTBY_KEYWORD3 + " " + SortCommand.REVERSE_KEYWORD) instanceof SortCommand);
        assertTrue(parser.parseCommand(SortCommand.COMMAND_WORD + " "
                + SortCommand.SORTBY_KEYWORD4 + " " + SortCommand.REVERSE_KEYWORD) instanceof SortCommand);
    }

    @Test
    public void parserCommand_addContactEvent() throws Exception {
        String description = "sleep";
        String startDateTime = "2023-10-10 10:00";
        String endDateTime = "2023-10-10 12:00";
        String validArg = "d/" + description
                + " ts/" + startDateTime
                + " te/" + endDateTime;
        EventBuilder eventBuilder = new EventBuilder();
        eventBuilder.withDescription(description);
        eventBuilder.withStartEndDate(startDateTime, endDateTime);
        AddContactEventCommand command =
                (AddContactEventCommand) parser.parseCommand(AddContactEventCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + validArg);

        assertEquals(command, new AddContactEventCommand(INDEX_FIRST_PERSON, eventBuilder.build()));
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
