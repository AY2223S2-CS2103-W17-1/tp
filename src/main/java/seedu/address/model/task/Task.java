package seedu.address.model.task;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

import seedu.address.model.person.Name;

/**
 * Represents a Task in the address book.
 * Guarantees: immutable; fields are validated; details are present and not null;
 */
public class Task implements Comparable<Task> {

    public static final String MESSAGE_CONSTRAINTS =
            "Name of tasks should only contain alphanumeric characters and spaces, and it should not be blank";

    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";


    // Identity field(s)
    public final Name taskName;

    // Data field(s)
    private TaskStatus status;

    private LocalDateTime creationDate;

    /**
     * Constructs a {@code Task}.
     *
     * @param taskName A valid Task name.
     */
    public Task(Name taskName) {
        requireNonNull(taskName);
        this.taskName = taskName;
        this.status = TaskStatus.INPROGRESS;
        this.creationDate = LocalDateTime.now();
    }

    /**
     * Marks the task as completed.
     */
    public void markTaskAsComplete() {
        status = TaskStatus.COMPLETE;
    }

    /**
     * Marks the task as late.
     */
    public void markTaskAsLate() {
        status = TaskStatus.LATE;
    }

    /**
     * Marks the task as in progress.
     */
    public void markTaskAsInProgress() {
        status = TaskStatus.INPROGRESS;
    }

    public Name getName() {
        return taskName;
    }

    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Returns true if both tasks have the same name.
     * This defines a weaker notion of equality between two tasks.
     */
    public boolean isSameTask(Task otherTask) {
        if (otherTask == this) {
            return true;
        }

        return otherTask != null
                && otherTask.getName().equals(getName());
    }

    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidTaskName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Task // instanceof handles nulls
                        && taskName.equals(((Task) other).taskName)); // state check
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, status, creationDate);
    }

    @Override
    public int compareTo(Task other) {
        if (this.status.equals(other.status)) {
            return this.creationDate.compareTo(other.creationDate);
        }
        return this.status.compareTo(other.status);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; Status: ")
                .append(getStatus());

        return builder.toString();
    }

}
