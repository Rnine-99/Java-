import java.io.BufferedReader;
import java.io.IOException;

public class ClientReader extends Thread{
    private final BufferedReader in;

    public ClientReader(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(in.readLine());
            }
        } catch (IOException e) {
            System.out.println("EXIT!");
        }
    }
}
