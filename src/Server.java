import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class Server {
    protected static final List<Socket> sockets = new Vector<>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9999);
        boolean flag = true;
        // 服务端广播写线程
        Thread writeThread = new Thread(new ServerWriteThread());
        writeThread.start();
        while (flag) {
            try {
                Socket accept = server.accept();
                synchronized (sockets) {
                    sockets.add(accept);
                }
                // 客户端线程启动
                Thread thread = new Thread(new ServerThread(accept));
                thread.start();
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        }
        server.close();
    }

}