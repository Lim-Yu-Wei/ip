import java.util.Scanner;

/**
 * A chatbot that keeps track of a list of tasks entered by the user.
 *
 * <p>This is the Level-3 increment: tasks can be added, listed, and marked
 * as done or not done. The chatbot runs until the user enters "bye".
 */
public class YuWei {
    /** The name the chatbot introduces itself with. */
    private static final String BOT_NAME = "YuWei";

    /** The horizontal rule printed around each block of output. */
    private static final String DIVIDER =
            "    ____________________________________________________________";

    /** The maximum number of tasks that can be stored. */
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        String[] tasks = new String[MAX_TASKS];
        boolean[] completed = new boolean[MAX_TASKS];
        int index = 0;

        // Greet the user on startup.
        System.out.println(DIVIDER);
        System.out.println("     Hello! I'm " + BOT_NAME);
        System.out.println("     What can I do for you?");
        System.out.println(DIVIDER);
        System.out.println();

        String line = in.nextLine();
        while (!line.equals("bye")) {
            String[] parts = line.split(" ");
            System.out.println(DIVIDER);

            switch (parts[0]) {
                case "list" -> {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < index; i++) {
                        System.out.println("     " + (i + 1) + ".["
                                + (completed[i] ? "X" : " ") + "] " + tasks[i]);
                    }
                }
                case "mark" -> {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if (taskNumber >= 0 && taskNumber < index) {
                        completed[taskNumber] = true;
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       [X] " + tasks[taskNumber]);
                    } else {
                        System.out.println("     Sorry, that task does not exist!");
                    }
                }
                case "unmark" -> {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if (taskNumber >= 0 && taskNumber < index) {
                        completed[taskNumber] = false;
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       [ ] " + tasks[taskNumber]);
                    } else {
                        System.out.println("     Sorry, that task does not exist!");
                    }
                }
                default -> {
                    tasks[index++] = line;
                    System.out.println("     added: " + line);
                }
            }

            System.out.println(DIVIDER);
            System.out.println();
            line = in.nextLine();
        }

        // Say goodbye before the program ends.
        System.out.println(DIVIDER);
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
