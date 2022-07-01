import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWriteThread extends Server implements Runnable {


    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String line = reader.readLine();
            while (!"end".equalsIgnoreCase(line)) {
                radio("Server Radio: "+line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void radio(String msg) throws IOException {
        PrintWriter out;
        synchronized (Server.sockets) {
            for (Socket sc : Server.sockets) {
                out = new PrintWriter(sc.getOutputStream());
                out.println(msg);
                out.flush();
            }
        }
    }
}