package tech.tengshe789.writenRMIcode;
public class UserServer extends User {
    public static void main(String[] args) {
        UserServer server =new UserServer();
        server.setAge(18);

        User_skeleton skeleton=new User_skeleton(server);

        skeleton.start();
    }
}
