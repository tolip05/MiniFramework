package framework.io;

public class ConsoleWriter implements Writer {
    @Override
    public void writeLine(String message) {
        System.out.println(message);
    }
}
