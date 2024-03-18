package project;

public class Messenger {
    private static String message = "";
    public static String getMessage() {
        return message;
    }

    public static void AddMessage(String text) {
        message += text + "\n";
    }
    public static void DeleteMessages() {
        message = "";
    }
}