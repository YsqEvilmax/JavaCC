package se701;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StudentSample {

    public static void main(String[] args) {
        Test t = new Test();
    }

    class Test {

        public Map states1 =  new HashMap<String, String>();
        states1.put("AL", "Alabama");
        states1.put("AK", "Alaska");
        states1.put("AZ", "Arizona");
        states1.put("WY", "Wyoming");

        public Test() {
        }
    }
}
