package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.WriteToExcel;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.participant.Person;

import java.util.List;

import static java.util.Objects.requireNonNull;

public class ImportCommand extends Command{
    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Imports contacts from an Excel file.\n"
            + "It imports only non-duplicate participants.\n"
            + "NOTE: FOP_MANAGER_LIST file name and location and sheet name and headers should NOT be altered.\n"
            + "If you want to update the whole FOP Manager, enter clear and then import.\n"
            + "To keep data safe om case pf accidental overwrite, export the data and copy somewhere else.\n";

    public static final String MESSAGE_SUCCESS = "The new names in the excel file have been imported. Should you " +
            "wish to update all the participants' particulars in the FOP Manager, enter the clear command followed " +
            "by import command.";
    @Override
    public CommandResult execute (Model model, CommandHistory commandHistory) {
        requireNonNull(model);
        String message = null;
        try {

            List<Person> persons = WriteToExcel.readFromExcel();
            if (persons.size() >=0) {

                model.addListUniquePerson(persons);
                model.commitAddressBook();
                message = String.format(MESSAGE_SUCCESS);
            } else {
                message = Messages.MESSAGE_UNSUCCESSFUL_IMPORT;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommandResult(message);
    }

}
