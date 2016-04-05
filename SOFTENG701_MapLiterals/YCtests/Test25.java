public class Test25 {

    class Car extends Vehicle {

        public void getSpeed() {
            int s = speed;
            int i = id;
        }
    }

    class Vehicle extends Object {

        protected int speed;

        public void drive() {
        }
    }

    class Object {

        protected int id;
    }
}
