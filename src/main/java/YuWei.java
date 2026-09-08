import java.util.Scanner;

/**
 * A command-line chatbot that keeps track of a list of tasks entered by the user.
 *
 * <p>Supported commands: {@code list}, {@code mark}, {@code unmark}, {@code todo},
 * {@code deadline}, {@code event} and {@code bye}.
 */
public class YuWei {
    private static final String BOT_NAME = "YuWei";
    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private static final String EXIT_COMMAND = "bye";
    private static final String MISSING_TASK_NUMBER_MESSAGE = "Please tell me which task number to mark or unmark.";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        printGreeting();

        String line = in.nextLine();
        while (!line.equals(EXIT_COMMAND)) {
            System.out.println(DIVIDER);
            try {
                taskCount = executeCommand(line, tasks, taskCount);
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
     * Executes the command contained in the given input line.
     *
     * @return the number of tasks in the list after the command has run, which
     *         differs from {@code taskCount} only when a task was added.
     */
    private static int executeCommand(String line, Task[] tasks, int taskCount) throws YuWeiException {
        String[] commandAndArgument = line.split(" ", 2);
        String command = commandAndArgument[0];
        int updatedTaskCount = taskCount;

        switch (command) {
            case "list" -> listTasks(tasks, taskCount);
            case "mark" -> markTask(tasks, taskCount,
                    requireArgument(commandAndArgument, MISSING_TASK_NUMBER_MESSAGE));
            case "unmark" -> unmarkTask(tasks, taskCount,
                    requireArgument(commandAndArgument, MISSING_TASK_NUMBER_MESSAGE));
            case "todo", "deadline", "event" -> {
                String argument = requireArgument(commandAndArgument,
                        "The description of a " + command + " cannot be empty.");
                tasks[updatedTaskCount] = createTask(command, argument);
                updatedTaskCount++;
                printTaskAdded(tasks[updatedTaskCount - 1], updatedTaskCount);
            }
            default -> throw new YuWeiException("I'm sorry, but I don't know what that means :-(");
        }

        return updatedTaskCount;
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
    private static Task createTask(String command, String argument) {
        return switch (command) {
            case "todo" -> new ToDo(argument);
            case "deadline" -> createDeadline(argument);
            case "event" -> createEvent(argument);
            // Unreachable: executeCommand only calls this for the three types above.
            default -> throw new IllegalArgumentException("Unknown task type: " + command);
        };
    }

    /** Creates a Deadline from an argument of the form {@code <description> /by <time>}. */
    private static Deadline createDeadline(String argument) {
        String[] descriptionAndBy = argument.split(" /by ", 2);
        return new Deadline(descriptionAndBy[0], descriptionAndBy[1]);
    }

    /** Creates an Event from an argument of the form {@code <description> /from <t> /to <t>}. */
    private static Event createEvent(String argument) {
        String[] descriptionAndTimes = argument.split(" /from ", 2);
        String[] fromAndTo = descriptionAndTimes[1].split(" /to ");
        return new Event(descriptionAndTimes[0], fromAndTo[0], fromAndTo[1]);
    }

    private static void listTasks(Task[] tasks, int taskCount) {
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println("     " + (i + 1) + ". " + tasks[i]);
        }
    }

    private static void markTask(Task[] tasks, int taskCount, String taskNumber) {
        int taskIndex = Integer.parseInt(taskNumber) - 1;
        if (!isExistingTask(taskIndex, taskCount)) {
            printTaskNotFound();
            return;
        }
        tasks[taskIndex].markAsDone();
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println("       " + tasks[taskIndex]);
    }

    private static void unmarkTask(Task[] tasks, int taskCount, String taskNumber) {
        int taskIndex = Integer.parseInt(taskNumber) - 1;
        if (!isExistingTask(taskIndex, taskCount)) {
            printTaskNotFound();
            return;
        }
        tasks[taskIndex].markAsNotDone();
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       " + tasks[taskIndex]);
    }

    /** Returns true if {@code taskIndex} refers to a task currently in the list. */
    private static boolean isExistingTask(int taskIndex, int taskCount) {
        return taskIndex >= 0 && taskIndex < taskCount;
    }

    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task: " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void printTaskNotFound() {
        System.out.println("     Sorry, that task does not exist!");
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
