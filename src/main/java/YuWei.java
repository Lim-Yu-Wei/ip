import java.util.Scanner;



public class YuWei {
    /** The name the chatbot introduces itself with. */
    private static final String BOT_NAME = "YuWei";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Hello! I'm " + BOT_NAME);
        System.out.println("What can I do for you?");
        String line = in.nextLine();
        while (!line.equals("bye")) {
            System.out.println(line);
            line = in.nextLine();
        }
        // Say goodbye before the program ends.
        System.out.println("Bye. Hope to see you again soon!");
    }
}
