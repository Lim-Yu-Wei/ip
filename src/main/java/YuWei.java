import java.util.Scanner;



public class YuWei {
    /** The name the chatbot introduces itself with. */
    private static final String BOT_NAME = "YuWei";


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Hello! I'm " + BOT_NAME);
        System.out.println("What can I do for you?");
        String line = in.nextLine();
        String[] tasks = new String[100];
        boolean[] completed = new boolean[100];
        int index = 0;

        while (!line.equals("bye")) {
            String[] parts = line.split(" ");
            switch (parts[0]) {

                case "list" -> {
                    for(int i = 0; i < index; i++){
                        System.out.println((i+1) + "." + "[" + (completed[i] ? "X" : " ") + "] " + tasks[i]);
                    }

                }
                case "mark" -> {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if (taskNumber < index && !completed[taskNumber]) {
                        completed[taskNumber] = true;
                        System.out.println("Nice! I've marked this task as done:\n   [X] " + tasks[taskNumber]);
                    }else{
                        System.out.println("Sorry, this task is either completed or doesnt exist!");
                    }
                }

                case "unmark" -> {
                    int taskNumber = Integer.parseInt(parts[1]) - 1;
                    if(taskNumber < index && completed[taskNumber]){
                        completed[taskNumber+1] = false;
                        System.out.println("OK, I've marked this task as not done yet:\n   [ ] " +  tasks[taskNumber]);
                    }else{
                        System.out.println("Sorry, this task is either still outstanding or doesnt exist!");
                    }

                }
                default -> {
                    System.out.println("added: " + line);

                    tasks[index++] = line;

                }

            }
            line = in.nextLine();

        }
        // Say goodbye before the program ends.
        System.out.println("Bye. Hope to see you again soon!");
    }
}
