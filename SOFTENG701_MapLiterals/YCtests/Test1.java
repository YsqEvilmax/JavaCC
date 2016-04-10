public class EnclosingClass {

    public void fun() {
        int a = 0;
        while (true) {
            int a = 1;
        }
        while (a == 0) {
            int b = 1;
            a = b;
        }
        b++;
        this.a++;
    }

    int a;
}
