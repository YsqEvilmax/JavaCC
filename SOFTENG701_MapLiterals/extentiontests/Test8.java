package se701;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StudentSample {

    public static void main(String[] args) {
        Test t = new Test();
        Map<String, String> x = t.getMap();
    }

    public class Test {

        private Map<String, String> maptest;

        public Map<String, String> getMap() {
            maptest =  new HashMap<String, String>();
            maptest.put("AL", "Alabama");
            maptest.put("AK", "Alaska");
            maptest.put("AZ", "Arizona");
            maptest.put("WY", "Wyoming");
            return maptest;
        }
    }
}
