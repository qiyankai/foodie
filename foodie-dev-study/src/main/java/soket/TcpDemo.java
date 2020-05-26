package soket;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpDemo {
    public static void main(String[] args) {
        try {
            // 创建一个ServerSocket,监听Client端发来的数据
            ServerSocket serverSocket = new ServerSocket(65000);
            Socket socket = serverSocket.accept();
            new TcpServerThread(socket).run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class TcpServerThread extends Thread {
    private Socket socket;

    public TcpServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // TCP是有连接的，取出socket对应的输入、输出流
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // 读取输入流（client发来的消息）
            int len = 0;
            byte[] bufferArr = new byte[1024];
            len = inputStream.read(bufferArr);
            String content = new String(bufferArr, 0, len);

            // 业务xxx，此处为打印发来的字符串，并计算字符串长度
            System.out.println(content);

            // 将数据封装成byte[]，通过输出流传回客户端
            String sendData = String.valueOf(content.length());
            outputStream.write(sendData.getBytes(),0,sendData.length());
            outputStream.close();
            inputStream.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class TcpClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 65000);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            byte[] sendData = "服务端你好！".getBytes();
            outputStream.write(sendData,0,sendData.length);
            outputStream.flush();

            int len = 0;
            byte[] bufferArr = new byte[1024];
            len = inputStream.read(bufferArr);
            String content = new String(bufferArr, 0, len);
            System.out.println(content);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}


