package yuwei;

import yuwei.task.Deadline;
import yuwei.task.Event;
import yuwei.task.Task;
import yuwei.task.ToDo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A command-line chatbot that keeps track of a list of tasks entered by the user.
 * Tasks are saved to {@code ./data/yuwei.txt} after every command and loaded again on startup.
 *
 * <p>Supported commands: {@code list}, {@code mark}, {@code unmark}, {@code delete},
 * {@code todo}, {@code deadline}, {@code event} and {@code bye}.
 */
public class YuWei {
    private static final String BOT_NAME = "YuWei";
    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final String EXIT_COMMAND = "bye";
    private static final String MISSING_TASK_NUMBER_MESSAGE = "Please tell me which task number.";
    private static final String EVENT_FORMAT_MESSAGE =
            "An event needs a start and an end. Try: event <description> /from <start> /to <end>";
    /** Relative path built with Path.of, so it works on any computer and any OS. */
    private static final Path DATA_FILE = Path.of("data", "yuwei.txt");

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Storage storage = new Storage(DATA_FILE);
        // An ArrayList grows as tasks are added and tracks its own size, so no separate count is needed.
        ArrayList<Task> tasks = new ArrayList<>();

        printGreeting();

        try {
            tasks.addAll(storage.load());
        } catch (YuWeiException e) {
            System.out.println("     " + e.getMessage());
        }

        String line = in.nextLine();
        while (!line.equals(EXIT_COMMAND)) {
            System.out.println(DIVIDER);
            try {
                executeCommand(line, tasks);
                // Saving after every command is simplest and guarantees no change is lost.
                storage.save(tasks);
            } catch (YuWeiException e) {
                System.out.println("     " + e.getMessage());
            }
            System.out.println(DIVIDER);
            System.out.println();
            line = in.nextLine();
        }

        printFarewell();
    }

    /**
     * Executes the command contained in the given input line, updating {@code tasks} if needed.
     */
    private static void executeCommand(String line, ArrayList<Task> tasks) throws YuWeiException {
        String[] commandAndArgument = line.split(" ", 2);
        String command = commandAndArgument[0];

        switch (command) {
            case "list" -> listTasks(tasks);
            case "mark" -> markTask(tasks,
                    requireArgument(commandAndArgument, MISSING_TASK_NUMBER_MESSAGE));
            case "unmark" -> unmarkTask(tasks,
                    requireArgument(commandAndArgument, MISSING_TASK_NUMBER_MESSAGE));
            case "delete" -> deleteTask(tasks,
                    requireArgument(commandAndArgument, MISSING_TASK_NUMBER_MESSAGE));
            case "todo", "deadline", "event" -> {
                String argument = requireArgument(commandAndArgument,
                        "The description of a " + command + " cannot be empty.");
                Task task = createTask(command, argument);
                tasks.add(task);
                printTaskAdded(task, tasks.size());
            }
            default -> throw new YuWeiException("I'm sorry, but I don't know what that means :-(");
        }
    }

    /**
     * Returns the argument that follows the command word.
     *
     * @throws YuWeiException with {@code errorMessage} if no argument was given, or it is blank
     */
    private static String requireArgument(String[] commandAndArgument, String errorMessage)
            throws YuWeiException {
        if (commandAndArgument.length < 2 || commandAndArgument[1].isBlank()) {
            throw new YuWeiException(errorMessage);
        }
        return commandAndArgument[1];
    }

    /**
     * Creates the task described by {@code argument}, of the type named by {@code command}.
     */
    private static Task createTask(String command, String argument) throws YuWeiException {
        return switch (command) {
            case "todo" -> new ToDo(argument);
            case "deadline" -> createDeadline(argument);
            case "event" -> createEvent(argument);
            // Unreachable: executeCommand only calls this for the three types above.
            default -> throw new IllegalArgumentException("Unknown task type: " + command);
        };
    }

    /** Creates a Deadline from an argument of the form {@code <description> /by <time>}. */
    private static Deadline createDeadline(String argument) throws YuWeiException {
        String[] descriptionAndBy = argument.split(" /by ", 2);
        if (descriptionAndBy.length < 2) {
            throw new YuWeiException("A deadline needs a time. Try: deadline <description> /by <time>");
        }
        return new Deadline(descriptionAndBy[0], descriptionAndBy[1]);
    }

    /** Creates an Event from an argument of the form {@code <description> /from <t> /to <t>}. */
    private static Event createEvent(String argument) throws YuWeiException {
        String[] descriptionAndTimes = argument.split(" /from ", 2);
        if (descriptionAndTimes.length < 2) {
            throw new YuWeiException(EVENT_FORMAT_MESSAGE);
        }
        String[] fromAndTo = descriptionAndTimes[1].split(" /to ", 2);
        if (fromAndTo.length < 2) {
            throw new YuWeiException(EVENT_FORMAT_MESSAGE);
        }
        return new Event(descriptionAndTimes[0], fromAndTo[0], fromAndTo[1]);
    }

    private static void listTasks(ArrayList<Task> tasks) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + ". " + tasks.get(i));
        }
    }

    private static void markTask(ArrayList<Task> tasks, String taskNumber) throws YuWeiException {
        int taskIndex = parseTaskIndex(taskNumber, tasks.size());
        tasks.get(taskIndex).markAsDone();
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println("       " + tasks.get(taskIndex));
    }

    private static void unmarkTask(ArrayList<Task> tasks, String taskNumber) throws YuWeiException {
        int taskIndex = parseTaskIndex(taskNumber, tasks.size());
        tasks.get(taskIndex).markAsNotDone();
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       " + tasks.get(taskIndex));
    }

    /** Removes the task with the given number from the list and reports what was removed. */
    private static void deleteTask(ArrayList<Task> tasks, String taskNumber) throws YuWeiException {
        int taskIndex = parseTaskIndex(taskNumber, tasks.size());
        Task removedTask = tasks.remove(taskIndex); // remove() also returns the removed task
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + removedTask);
        System.out.println("     Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Returns true if {@code taskIndex} refers to a task currently in the list. */
    private static boolean isExistingTask(int taskIndex, int taskCount) {
        return taskIndex >= 0 && taskIndex < taskCount;
    }

    /**
     * Converts a user-supplied task number into an index into the task list.
     *
     * @throws YuWeiException if the text is not a number or does not refer to an existing task
     */
    private static int parseTaskIndex(String taskNumber, int taskCount) throws YuWeiException {
        int taskIndex;
        try {
            taskIndex = Integer.parseInt(taskNumber.trim()) - 1;
        } catch (NumberFormatException e) {
            throw new YuWeiException("'" + taskNumber + "' is not a task number.");
        }
        if (!isExistingTask(taskIndex, taskCount)) {
            throw new YuWeiException("There is no task " + (taskIndex + 1) + " in your list.");
        }
        return taskIndex;
    }

    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " tasks in the list.");
    }

    private static void printGreeting() {
        System.out.println(DIVIDER);
        System.out.println("     Hello! I'm " + BOT_NAME);
        System.out.println("     What can I do for you?");
        System.out.println(DIVIDER);
        System.out.println();
    }

    private static void printFarewell() {
        System.out.println(DIVIDER);
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
