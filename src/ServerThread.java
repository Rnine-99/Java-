import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 服务器线程，主要来处理多个客户端的请求
 */
public class ServerThread extends Server implements Runnable {

    Socket socket;
    String socketName;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //设置该客户端的端点地址
            socketName = socket.getRemoteSocketAddress().toString();
            System.out.println("Client@" + socketName + "join the chatroom");
            radio("Client@" + socketName + "join the chatroom");
            boolean flag = true;
            while (flag) {
                //阻塞，等待该客户端的输出流
                String line = reader.readLine();
                //若客户端退出，则退出连接。
                if (line == null) {
                    flag = false;
                    continue;
                }
                String msg = "Client@" + socketName + ":" + line;
                System.out.println(msg);
                //向在线客户端输出信息
                radio(msg);
            }

            closeConnect();
        } catch (IOException e) {
            try {
                closeConnect();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void radio(String msg) throws IOException {
        PrintWriter out = null;
        synchronized (Server.sockets) {
            for (Socket sc : Server.sockets) {
                if (socket.equals(sc))
                    continue;
                out = new PrintWriter(sc.getOutputStream());
                out.println(msg);
                out.flush();
            }
        }
    }

    /**
     * 关闭该socket的连接
     *
     */
    public void closeConnect() throws IOException {
        System.out.println("Client@" + socketName + "已退出聊天");
        radio("Client@" + socketName + "已退出聊天");
        //移除没连接上的客户端
        synchronized (Server.sockets) {
            Server.sockets.remove(socket);
        }
        socket.close();
    }
}