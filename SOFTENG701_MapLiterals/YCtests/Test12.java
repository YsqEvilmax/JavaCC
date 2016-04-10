public class Test12 {

    public int a;

    public void fun() {
        Child ben = new Child();
        ben.b = ben.a;
    }

    public class Child {

        public int b;
    }
}
