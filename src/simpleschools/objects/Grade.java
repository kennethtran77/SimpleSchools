package simpleschools.objects;

import simpleschools.SimpleSchools;

/**
 *
 * @author Kenneth Tran
 */
public class Grade {
    
    private int value;
    
    public Grade(int value) {
        this.value = value;
    }
    
    public void setValue(int value) {
        this.value = value < 0 ? 0 : value;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void unregister(SimpleSchools main) {
        main.grades.remove(this);
        // Add to toPurge
        main.toPurge.add(main.gradesPath.toString() + "/" + this.getValue() + ".json");
        
        // Unregister Courses registered with this Grade
        main.courses.stream().filter(c -> c.getGrade().equals(this)).forEach(c -> c.unregister(main));
    }
    
    @Override
    public String toString() {
        return this.value + "";
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Grade))
            return false;
        
        return ((Grade) o).getValue() == this.value;
    }

    @Override
    public int hashCode() {
        return this.value;
    }
    
}