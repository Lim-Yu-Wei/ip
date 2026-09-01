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
            String[] parts = line.split(" ", 2);
            System.out.println(DIVIDER);


            switch (parts[0]) {
                case "list" -> {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < index; i++) {
                        System.out.println("     " + (i + 1) + ". " + tasks[i]);
                    }
                }
                case "mark" -> {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if (taskNumber >= 0 && taskNumber < index) {
                        tasks[taskNumber].markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + tasks[taskNumber]);
                    } else {
                        System.out.println("     Sorry, that task does not exist!");
                    }
                }
                case "unmark" -> {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if (taskNumber >= 0 && taskNumber < index) {
                        tasks[taskNumber].markAsNotDone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + tasks[taskNumber]);
                    } else {
                        System.out.println("     Sorry, that task does not exist!");
                    }
                }
                case "todo" -> {
                    tasks[index++] = new ToDo(parts[1]);
                    System.out.println("Got it. I've added this task: " + tasks[index - 1]);
                    System.out.println("Now you have " + index + " tasks in the list.");
                }
                case "deadline" -> {
                    String[] descriptionAndBy = parts[1].split(" /by ", 2);
                    tasks[index++] = new Deadline(descriptionAndBy[0], descriptionAndBy[1]);
                    System.out.println("Got it. I've added this task: " + tasks[index - 1]);
                    System.out.println("Now you have " + index + " tasks in the list.");
                }

                case "event" -> {
                    String[] description = parts[1].split(" /from ", 2);
                    String[] fromAndTo = description[1].split(" /to ");
                    tasks[index++] = new Event(description[0], fromAndTo[0], fromAndTo[1]);
                    System.out.println("Got it. I've added this task: " + tasks[index - 1]);
                    System.out.println("Now you have " + index + " tasks in the list.");
                }
//                default -> {
//                    tasks[index++] = new Task(line);
//                    System.out.println("     added: " + line);
//                }
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
