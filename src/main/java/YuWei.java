import java.util.Scanner;

public class YuWei {
    private static final String BOT_NAME = "YuWei";
    private static final String DIVIDER =
            "    ____________________________________________________________";
    private static final int MAX_TASKS = 100;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Task[] tasks = new Task[MAX_TASKS];
        int index = 0;

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
                                + tasks[i].getStatusIcon() + "] "
                                + tasks[i].getDescription());
                    }
                }
                case "mark" -> {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if (taskNumber >= 0 && taskNumber < index) {
                        tasks[taskNumber].markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       [" + tasks[taskNumber].getStatusIcon()
                                + "] " + tasks[taskNumber].getDescription());
                    } else {
                        System.out.println("     Sorry, that task does not exist!");
                    }
                }
                case "unmark" -> {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if (taskNumber >= 0 && taskNumber < index) {
                        tasks[taskNumber].markAsNotDone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       [" + tasks[taskNumber].getStatusIcon()
                                + "] " + tasks[taskNumber].getDescription());
                    } else {
                        System.out.println("     Sorry, that task does not exist!");
                    }
                }
                default -> {
                    tasks[index++] = new Task(line);
                    System.out.println("     added: " + line);
                }
            }

            System.out.println(DIVIDER);
            System.out.println();
            line = in.nextLine();
        }

        System.out.println(DIVIDER);
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }
}
