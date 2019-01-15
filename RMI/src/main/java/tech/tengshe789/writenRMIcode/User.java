package tech.tengshe789.writenRMIcode;

import java.io.IOException;

/**
 * 假设这是个接口
 */
public class User {
    private int age;

    public int getAge() throws IOException {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
