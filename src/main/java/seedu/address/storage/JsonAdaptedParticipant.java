package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;

import seedu.address.model.grouping.Group;
import seedu.address.model.participant.Birthday;
import seedu.address.model.participant.Email;
import seedu.address.model.participant.Major;
import seedu.address.model.participant.Name;
import seedu.address.model.participant.Participant;
import seedu.address.model.participant.Phone;
import seedu.address.model.participant.Sex;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Participant}.
 */
class JsonAdaptedParticipant {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Participant's %s field is missing!";

    private final String name;
    private final String sex;
    private final String birthday;
    private final String phone;
    private final String email;
    private final String major;
    private final String group;
    private final String house;
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedParticipant} with the given participant details.
     */
    @JsonCreator
    public JsonAdaptedParticipant(@JsonProperty("name") String name, @JsonProperty("sex") String sex,
                                  @JsonProperty("birthday") String birthday, @JsonProperty("phone") String phone,
                                  @JsonProperty("email") String email, @JsonProperty("major") String major,
                                  @JsonProperty("group") String group, @JsonProperty("house") String house,
                                  @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.major = major;
        this.group = group;
        this.house = house;
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Participant} into this class for Jackson use.
     */
    public JsonAdaptedParticipant(Participant source) {
        name = source.getName().fullName;
        sex = source.getSex().value;
        birthday = source.getBirthday().value;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        major = source.getMajor().value;
        group = source.getGroup().getGroupName();
        house = source.getGroup().getHouseName();
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted participant object into the model's {@code Participant} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted participant.
     */
    public Participant toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (sex == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Sex.class.getSimpleName()));
        }
        if (!Sex.isValidSex(sex)) {
            throw new IllegalValueException(Sex.MESSAGE_CONSTRAINTS);
        }
        final Sex modelSex = new Sex(sex);

        if (birthday == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Birthday.class.getSimpleName()));
        }
        if (!Birthday.isValidBirthday(birthday)) {
            throw new IllegalValueException(Birthday.MESSAGE_CONSTRAINTS);
        }
        final Birthday modelBirthday = new Birthday(birthday);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (major == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Major.class.getSimpleName()));
        }
        if (!Major.isValidMajor(major)) {
            throw new IllegalValueException(Major.MESSAGE_CONSTRAINTS);
        }
        final Major modelMajor = new Major(major);

        if (group == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Group.class.getSimpleName()));
        }
        if (!Group.isValidGroup(group)) {
            throw new IllegalValueException(Group.MESSAGE_CONSTRAINTS);
        }
        final Group modelGroup = new Group(group, house);

        final Set<Tag> modelTags = new HashSet<>(personTags);
        return new Participant(modelName, modelSex, modelBirthday, modelPhone, modelEmail,
                modelMajor, modelGroup, modelTags);
    }

}
