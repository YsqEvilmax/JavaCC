package se701;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StudentSample {

    public static void main(String[] args) {
        sampleMethod();
    }

    public static void sampleMethod() {
        Map<String, String> states1 = new HashMap<String, String>();
        states1.put("AL", "Alabama");
        states1.put("AK", "Alaska");
        states1.put("AZ", "Arizona");
        states1.put("WY", "Wyoming");
        Map<Integer, String> states2 = new HashMap<Integer, String>();
        states2.put(1, "Alabama");
        states2.put(2, "Alaska");
        states2.put(3, "Arizona");
        states2.put(4, "Wyoming");
        Map<String, String> states3 = new HashMap<String, String>();
        states3.put("AL", "Alabama");
        states3.put("AK", "Alaska");
        states3.put("AZ", "Arizona");
        states3.put("WY", "Wyoming");

    }
}
