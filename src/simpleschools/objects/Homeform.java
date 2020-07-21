package simpleschools.objects;

import java.util.Objects;
import simpleschools.SimpleSchools;

/**
 *
 * @author Kenneth Tran
 */
public class Homeform {
    
    String code;
    Class classroom;
    
    public Homeform(String code, Class c) {
        this.code = code;
        this.classroom = c;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public void setClass(Class c) {
        this.classroom = c;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public Grade getGrade() {
        return this.classroom.getCourse().getGrade();
    }
    
    public Class getClassroom() {
        return this.classroom;
    }
    
    public void unregister(SimpleSchools main) {
        main.homeforms.remove(this);
        // Add to toPurge
        main.toPurge.add(main.homeformsPath.toString() + "/" + this.getCode() + ".json");
        
        // Unregister Students registered with this Homeform
        main.students.stream().filter(s -> s.getHomeform().equals(this)).forEach(s -> s.unregister(main));
    }
    
    @Override
    public String toString() {
        return this.code;
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Homeform))
            return false;
        
        Homeform other = (Homeform) o;
        
        return other.code.equalsIgnoreCase(this.code);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.code);
    }
}