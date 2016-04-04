import java.io.*;

public class Test4 extends Test4_1 {

    public String b(String in) {
        return "hehe";
    }
}

public class Test4_1 implements Test4_2 {

    public String b(String in) {
        return in;
    }
}

public interface Test4_2 {

    public String b(String in);
}
