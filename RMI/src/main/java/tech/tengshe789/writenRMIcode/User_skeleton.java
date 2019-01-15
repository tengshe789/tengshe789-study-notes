package tech.tengshe789.writenRMIcode;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端骨架
 */
public class User_skeleton extends Thread{
    private UserServer server;

    public User_skeleton(UserServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        //通过serverSocket启动
        ServerSocket serverSocket =null;
        try {
            serverSocket=new ServerSocket(8888);//定义端口
            Socket socket=serverSocket.accept();//接受请求
            //请求不是空的话
            while (socket!=null){
                //得到输入的这个序列化
                ObjectInputStream read =new ObjectInputStream(socket.getInputStream());

                String method = (String) read.readObject();
                //如果拿到对象的age
                if (method.equals("age")){
                    int age =server.getAge();
                    ObjectOutputStream objectOutputStream =new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.write(age);
                    objectOutputStream.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket==null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
