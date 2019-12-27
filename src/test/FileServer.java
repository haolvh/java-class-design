package test;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class FileServer {
    public static void Start(JTextArea txtMsg) throws IOException {
        System.out.println("文件线程已启动");
        txtMsg.setText("服务器已启动, 绑定8086端口.");
        ServerSocket serverSocket = new ServerSocket(8086);
        while(true){
            final Socket socket=serverSocket.accept();
            new Thread(){
                public void run() {
                    try {
                        InputStream is = socket.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String filename = br.readLine();
                        File dir = new File("upload");
                        dir.mkdir();
                        File file = new File(dir, filename);
                        FileOutputStream fos = new FileOutputStream(file, true);
                        byte[] buffer = new byte[1024];
                        while (is.read(buffer) != -1)
                            fos.write(buffer);
                        fos.close();
                        txtMsg.setText( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+" 收到文件: " + file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

    }
}
