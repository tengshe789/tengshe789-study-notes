package tech.tengshe789.writenRMIcode;
import java.io.IOException;

public class UserClient {
    public static void main(String[] args) throws IOException {
        User user =new User_stub();
        user.getAge();
        System.out.println(user.getAge());

    }
}
