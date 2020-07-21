package simpleschools.objects;

import java.util.Objects;
import simpleschools.SimpleSchools;

/**
 *
 * @author Kenneth Tran
 */
public class Course {
    
    private String courseName, courseCode, description;
    private Grade grade;
    
    public Course(String courseName, String courseCode, String description, Grade grade) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.description = description;
        this.grade = grade;
    }
    
    public String getName() {
        return this.courseName;
    }
    
    public String getCode() {
        return this.courseCode;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public Grade getGrade() {
        return this.grade;
    }
    
    public void setName(String courseName) {
        this.courseName = courseName;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public void setGrade(Grade grade) {
        this.grade = grade;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void unregister(SimpleSchools main) {
        main.courses.remove(this);
        // Add to toPurge
        main.toPurge.add(main.coursesPath.toString() + "/" + this.getCode() + ".json");
        
        // Unregister Classes registered with this Course
        main.classes.stream().filter(c -> c.getCourse().equals(this)).forEach(c -> c.unregister(main));
    }
    
    @Override
    public String toString() {
        return this.courseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Course))
            return false;
        
        return this.courseCode.equalsIgnoreCase(((Course) o).courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.courseCode);
    }
    
}