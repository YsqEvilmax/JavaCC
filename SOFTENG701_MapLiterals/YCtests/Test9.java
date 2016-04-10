public class EnclosingClass {

    private int a;

    private int b;

    public String run() {
        b = 3;
        this.a = this.b;
        return "";
    }

    public void go() {
        a = run();
    }
}
