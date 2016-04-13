package se701;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StudentSample {

    public StudentSample s1 = new StudentSample();

    protected StudentSample s2 = new StudentSample();

    private StudentSample s3 = new StudentSample();

    public static void main(String[] args) {
        sampleMethod();
    }

    public static void sampleMethod() {
        Map states1 =  new HashMap<String, String>();
        states1.put("AL", "Alabama");
        states1.put("AK", "Alaska");
        states1.put("AZ", "Arizona");
        states1.put("WY", "Wyoming");
        Map states2 =  new HashMap<Integer, String>();
        states2.put(1, "Alabama");
        states2.put(2, "Alaska");
        states2.put(3, "Arizona");
        states2.put(4, "Wyoming");
        Map states3 =  new HashMap<StudentSample, String>();
        states3.put(s1, "Alabama");
        states3.put(s2, "Alaska");
        states3.put(s3, "Arizona");
    }
}
