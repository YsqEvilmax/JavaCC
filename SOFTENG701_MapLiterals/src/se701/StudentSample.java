package se701;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StudentSample {

    public static void main(String[] args) {
        sampleMethod();
    }

    public static void sampleMethod() {
        Map<String, String> states = new HashMap<String, String>();
        states.put("a", "Alabama");
        states.put("b", "Alaska");
        states.put("c", "Arizona");
        states.put("d", "Wyoming");
        for (Entry<String, String> state : states.entrySet()) {
            System.out.println(state.getKey() + " " + state.getValue());
        }
    }
}
