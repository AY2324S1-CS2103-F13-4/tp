package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DESCRIPTION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_END_DATE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_END_DATE_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_START_DATE_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDescription;
import seedu.address.model.event.EventPeriod;
import seedu.address.testutil.EventBuilder;

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Event expectedEvent = new EventBuilder().withDescription(VALID_DESCRIPTION)
                .withStartEndDate(VALID_START_DATE, VALID_END_DATE).build();
        String validUserInput = " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE;

        assertParseSuccess(parser, validUserInput, new AddEventCommand(expectedEvent));
    }

    @Test
    public void parse_repeatedPrefix_failure() {
        String userInputRepeatDescriptionPrefix = " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE;

        assertParseFailure(parser, userInputRepeatDescriptionPrefix,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EVENT_DESCRIPTION));

        String userInputRepeatEventStartDateTimePrefix = " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE;

        assertParseFailure(parser, userInputRepeatEventStartDateTimePrefix,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EVENT_START_DATE_TIME));

        String userInputRepeatEventEndDateTimePrefix = " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE;

        assertParseFailure(parser, userInputRepeatEventEndDateTimePrefix,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EVENT_END_DATE_TIME));

        String userInputRepeatMultiplePrefix = " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE;

        assertParseFailure(parser, userInputRepeatMultiplePrefix,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EVENT_DESCRIPTION,
                        PREFIX_EVENT_START_DATE_TIME, PREFIX_EVENT_END_DATE_TIME));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String exepectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

        String missingDescription = " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE;

        assertParseFailure(parser, missingDescription, exepectedMessage);

        String missingStartDate = " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE;

        assertParseFailure(parser, missingStartDate, exepectedMessage);

        String missingEndDate = " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE;

        assertParseFailure(parser, missingEndDate, exepectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        String userInputInvalidDescription = " " + PREFIX_EVENT_DESCRIPTION + INVALID_DESCRIPTION
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE;

        assertParseFailure(parser, userInputInvalidDescription,
                EventDescription.MESSAGE_CONSTRAINTS);

        String userInputInvalidStartDateTime = " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_START_DATE_TIME + INVALID_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + VALID_END_DATE;

        assertParseFailure(parser, userInputInvalidStartDateTime,
                EventPeriod.MESSAGE_CONSTRAINTS);

        String userInputInvalidEndDateTime = " " + PREFIX_EVENT_DESCRIPTION + VALID_DESCRIPTION
                + " " + PREFIX_EVENT_START_DATE_TIME + VALID_START_DATE
                + " " + PREFIX_EVENT_END_DATE_TIME + INVALID_DATE;

        assertParseFailure(parser, userInputInvalidEndDateTime,
                EventPeriod.MESSAGE_CONSTRAINTS);
    }
}
