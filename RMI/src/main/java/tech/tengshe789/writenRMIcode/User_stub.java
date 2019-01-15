package tech.tengshe789.writenRMIcode;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class User_stub extends User{
    //客户端socket
    private Socket socket;

    public User_stub() throws IOException {
        socket = new Socket("localhost",8888);
    }

    @Override
    public int getAge() throws IOException {

            ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject("age");
            objectOutputStream.flush();

            ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());
            return objectInputStream.readInt();


    }
}
