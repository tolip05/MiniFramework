package framework.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader implements Reader {
    @Override
    public String readLine() throws IOException {
        BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
}
