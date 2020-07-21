package simpleschools.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import simpleschools.SimpleSchools;

/**
 *
 * @author Kenneth Tran
 */
public class Student {
    
    private String firstName, middleName, lastName, email, phoneNumber;
    private Homeform homeform;
    private final int studentId;
    
    protected HashMap<Class, List<Entry>> entries;
    
    public Student(String firstName, String middleName, String lastName, String email, String phoneNumber, Homeform homeform, int studentId) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.homeform = homeform;
        
        this.studentId = studentId;
        
        this.entries = new HashMap<>();
    }

    /**
     * Gets a list of entries for a class
     * @param c
     * @return list of entries
     */
    public List<Entry> getEntries(Class c) {
        if (this.entries.get(c) == null || this.entries.get(c).isEmpty()) {
            this.entries.put(c, new ArrayList<>());
        }
        
        return this.entries.get(c);
    }

    /**
     * Sets the list of entries for a class
     * @param c
     * @param ents 
     */
    public void setEntries(Class c, List<Entry> ents) {
        this.entries.put(c, ents);
    }
    
    /**
     * Calculate the average for a given class
     * @param c
     * @return student average for Class c
     */
    public double calculateAverage(Class c) {
        if (this.entries.get(c) == null || this.entries.get(c).isEmpty()) {
            return 0;
        }
        
        List<Entry> ents = this.entries.get(c);
        
        // Calculate the sum of the weights of all entries
        double weightSum = 0;
        
        for (Entry entry : ents)
            if (entry.getMark() >= 0)
                weightSum += entry.getWeight();
        
        double avg = 0;
        
        // For each entry in class c, add to the average the entry's mark multiplied by the entry's weight divided by the sum of all weights
        for (Entry entry : ents) {
            if (entry.getMark() >= 0) {
                avg += entry.getMark() * (entry.getWeight() / weightSum);
            }
        }
        
        // Shorten the double
        int temp = (int)(avg * 10.0);
        avg = ((double) temp) / 10.0;
        
        return avg;
    }
    
    /**
     * Calculates this student's average for all their classe
     * @return student average
     */
    public double calculateAverage() {
        double avg = 0;
            
        // Sum the averages for all of the student's classes
        for (Class cl : entries.keySet()) {
            avg += calculateAverage(cl);
        }
        
        if (entries.keySet().size() > 0) {
            // Divide sum by number of classes
            avg /= entries.keySet().size();
            
            // Shorten the double
            int temp = (int)(avg * 10.0);
            avg = ((double) temp) / 10.0;
            return avg;
        }
            
        return 0;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public String getMiddleName() {
        return this.middleName;
    }
    
    public String getLastName() {
        return this.lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
    
    public String getEmail() {
        return this.email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
    public Grade getGrade() {
        return this.homeform.getGrade();
    }
    
    public Homeform getHomeform() {
        return this.homeform;
    }
    
    public int getStudentId() {
        return this.studentId;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setHomeform(Homeform homeform) {
        this.homeform = homeform;
    }

    public void unregister(SimpleSchools main) {
        main.students.remove(this);
        // Add to toPurge
        main.toPurge.add(main.studentsPath.toString() + "/" + this.getStudentId() + ".json");
        
        // Remove this Student from all their Classes
        main.getStudentClasses(this).forEach(c -> c.removeStudent(this));
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Student))
            return false;
        
        return ((Student) o).getStudentId() == this.studentId;
    }
    
    @Override
    public int hashCode() {
        return this.studentId;
    }
    
}