package yuwei.task;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "[X] " : "[ ] ");
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getType();

    /** Returns "1" if this task is done and "0" otherwise, as stored in the data file. */
    protected String getDoneFlag() {
        return isDone ? "1" : "0";
    }

    /** Returns this task as one line of the data file, e.g. {@code T | 1 | read book}. */
    public abstract String toFileString();

    @Override
    public String toString() {
        return getType() + getStatusIcon() + description;
    }
}
