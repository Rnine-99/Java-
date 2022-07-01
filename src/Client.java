import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // 读取服务器的信息
        ClientReader clientReader = new ClientReader(in);
        clientReader.start();
        // 写信息给客户端
        String line = reader.readLine();
        while (!"end".equalsIgnoreCase(line)) {
            out.println(line);
            out.flush();
            line = reader.readLine();
        }
        clientReader.interrupt();
        out.close();
        in.close();
        socket.close();
        System.exit(0);
    }
}