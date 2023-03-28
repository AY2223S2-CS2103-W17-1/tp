package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.student.Student;

/**
 * Represents the in-memory model of the math tutoring data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Mathutoring mathutoring;
    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;

    /**
     * Initializes a ModelManager with the given mathutoring and userPrefs.
     */
    public ModelManager(ReadOnlyMathutoring mathutoring, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(mathutoring, userPrefs);

        logger.fine("Initializing with math tutoring: " + mathutoring + " and user prefs " + userPrefs);

        this.mathutoring = new Mathutoring(mathutoring);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(this.mathutoring.getStudentList());
    }

    public ModelManager() {
        this(new Mathutoring(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getMathutoringFilePath() {
        return userPrefs.getMathutoringFilePath();
    }

    @Override
    public void setMathutoringFilePath(Path mathutoringFilePath) {
        requireNonNull(mathutoringFilePath);
        userPrefs.setMathutoringFilePath(mathutoringFilePath);
    }

    //=========== Mathutoring ================================================================================

    @Override
    public void setMathutoring(ReadOnlyMathutoring mathutoring) {
        this.mathutoring.resetData(mathutoring);
    }

    @Override
    public ReadOnlyMathutoring getMathutoring() {
        return mathutoring;
    }

    @Override
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return mathutoring.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        mathutoring.removeStudent(target);
    }

    @Override
    public void checkStudent(Student target) {
        mathutoring.checkStudent(target);
    }

    @Override
    public void addStudent(Student student) {
        mathutoring.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);

        mathutoring.setStudent(target, editedStudent);
    }

    //=========== Filtered Student List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Student} backed by the internal list of
     * {@code versionedMathutoring}
     */
    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return filteredStudents;
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    @Override
    public Student findSelectedStudent() {
        return mathutoring.findCheckedStudent();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return mathutoring.equals(other.mathutoring)
                && userPrefs.equals(other.userPrefs)
                && filteredStudents.equals(other.filteredStudents);
    }
}
