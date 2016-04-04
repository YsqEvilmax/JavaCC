public class Test5 {

    public void a() {
        int test = d(5);
        String testAgain = c(test);
        test = b(testAgain);
        test = d(1);
    }

    public int b(String num) {
        return 99;
    }

    public String c(int num) {
        return "" + num;
    }

    public int d(int x) {
        return x;
    }
}
