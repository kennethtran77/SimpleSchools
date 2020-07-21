package simpleschools.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import simpleschools.SimpleSchools;

/**
 *
 * @author Kenneth Tran
 */
public class Class {
    
    public final List<Student> students;
    private Course course;
    private String className;
    
    private Queue<Integer> load;
    
    public Class(Course course, String className) {
        this.course = course;
        this.className = className;
        
        this.students = new ArrayList<>();
    }

    public Class(Course course, String className, Queue<Integer> load) {
        this.course = course;
        this.className = className;
        
        this.students = new ArrayList<>();
        this.load = load;
    }
    
    public void setName(String className) {
        this.className = className;
    }
    
    public void setCourse(Course course) {
        this.course = course;
    }
    
    public String getName() {
        return this.className;
    }
    
    public Course getCourse() {
        return this.course;
    }
    
    public String getCourseName() {
        return this.course.getName();
    }
    
    public Grade getGrade() {
        return this.getCourse().getGrade();
    }
    
    public void addStudent(Student student) {
        this.students.add(student);
    }
    
    public void removeStudent(Student student) {
        this.students.remove(student);
        student.entries.remove(this);
    }
    
    /**
     * Loads student objects from load queue of Student Ids
     * @param main 
     */
    public void loadStudents(SimpleSchools main) {
        while (!load.isEmpty()) {
            int id = load.poll();
            this.students.add(main.students.stream().filter(s -> s.getStudentId() == id).findAny().orElse(null));
        }
    }

    public boolean containsStudent(Student student) {
        return this.students.contains(student);
    }
    
    /**
     * Calculate the average by calculating the average of all students in this class
     * @return class average
     */
    public double calculateAverage() {
        double avg = 0;
        
        for (Student student : students)
            avg += student.calculateAverage(this);
        
        if (students.size() > 0)
            avg /= students.size();
        
        return avg;
    }

    /**
     * Calculate the average of a student for this class
     * @param student
     * @return student average
     */
    public double calculateAverage(Student student) {
        return student.calculateAverage(this);
    }
    
    public void unregister(SimpleSchools main) {
        main.classes.remove(this);
        // Add to toPurge
        main.toPurge.add(main.classesPath.toString() + "/" + this.getName() + ".json");
        
        // Remove all Students from this Class
        this.students.forEach(c -> removeStudent(c));
    }

    @Override
    public String toString() {
        return this.className;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Class))
            return false;
        
        Class other = (Class) o;
        
        return this.className.equalsIgnoreCase(other.className);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.className);
    }
    
}