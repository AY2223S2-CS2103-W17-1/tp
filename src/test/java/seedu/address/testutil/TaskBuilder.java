package seedu.address.testutil;

import seedu.address.model.person.Name;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskStatus;

/**
 * A utility class to help with building Task objects.
 */
public class TaskBuilder {

    public static final String DEFAULT_TASK_NAME = "Complete A Math Exercise 1";
    public static final TaskStatus DEFAULT_TASK_STATUS = TaskStatus.INPROGRESS;

    private Name taskName;

    private TaskStatus taskStatus;

    /**
     * Creates a {@code ScoreBuilder} with the default details.
     */
    public TaskBuilder() {
        taskName = new Name(DEFAULT_TASK_NAME);
        taskStatus = DEFAULT_TASK_STATUS;
    }

    /**
     * Initializes the TaskBuilder with the data of {@code taskToCopy}.
     */
    public TaskBuilder(Task taskToCopy) {
        taskName = taskToCopy.getName();
        taskStatus = taskToCopy.getStatus();
    }

    /**
     * Sets the {@code Name} of the {@code Task} that we are building.
     */
    public TaskBuilder withName(String name) {
        this.taskName = new Name(name);
        return this;
    }

    /**
     * Sets the {@code TaskStatus} of the {@code Task} that we are building.
     */
    public TaskBuilder withStatus(TaskStatus status) {
        this.taskStatus = status;
        return this;
    }

    public Task build() {
        return new Task(taskName, taskStatus);
    }
}
