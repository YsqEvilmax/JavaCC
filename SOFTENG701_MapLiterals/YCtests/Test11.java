public class EnclosingClass {

    private int a;

    private int b;

    public class NestedClass {

        public void run() {
            b = a;
        }
    }
}
