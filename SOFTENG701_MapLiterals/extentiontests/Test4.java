package se701;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StudentSample {

    public static void main(String[] args) {
        sampleMethod();
    }

    public static void sampleMethod() {
        StudentSample s1 = new StudentSample(), s2 = new StudentSample(), s3 = new StudentSample();
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
        Map states3 =  new HashMap<Double, String>();
        states3.put(1.1, "Alabama");
        states3.put(2.2, "Alaska");
        states3.put(3.4, "Arizona");
        states3.put(4.6, "Wyoming");
        Map states4 =  new HashMap<Double, Double>();
        states4.put(1.1, 1.414);
        states4.put(2.2, 1.414);
        states4.put(3.4, 1.414);
        states4.put(4.6, 1.414);
        Map states5 =  new HashMap<StudentSample, String>();
        states5.put(s1, "Alabama");
        states5.put(s2, "Alaska");
        states5.put(s3, "Arizona");
    }
}
