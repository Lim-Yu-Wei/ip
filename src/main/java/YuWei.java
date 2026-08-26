/**
 * A chatbot that greets the user and then exits.
 *
 * <p>This is the Level-0 increment: no user input is read yet, so the
 * program simply prints a greeting followed by a farewell and terminates.
 */
public class YuWei {
    /** The name the chatbot introduces itself with. */
    private static final String BOT_NAME = "YuWei";

    public static void main(String[] args) {
        // Greet the user on startup.
        System.out.println("Hello! I'm " + BOT_NAME);
        System.out.println("What can I do for you?");

        // Say goodbye before the program ends.
        System.out.println("Bye. Hope to see you again soon!");
    }
}
