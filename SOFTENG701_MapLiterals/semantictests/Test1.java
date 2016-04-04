public class Test1 {

    int a;

    public void foo(boolean x) {
        a = 12;
        Test2 lol = new Test2();
        lol = new Test2();
    }

    public class Test2 {
    }
}
