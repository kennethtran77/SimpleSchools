package simpleschools;

import simpleschools.objects.Student;
import simpleschools.objects.Class;
import simpleschools.objects.Course;
import simpleschools.objects.Grade;
import simpleschools.objects.Homeform;

/**
 *
 * @author Kenneth Tran
 */
public enum Criteria {

    NAME {
        @Override
        public int compare(Student a, Student b) {
            String nameA = a.getLastName() + a.getFirstName();
            String nameB = b.getLastName() + b.getFirstName();
            
            int c = nameA.compareToIgnoreCase(nameB);
            
            if (c > 0)
                return 1;
            else if (c < 0)
                return -1;
            else
                return 0;
        }

        @Override
        public int compare(Class a, Class b) {
            int c = a.getName().compareToIgnoreCase(b.getName());
            
            if (c > 0)
                return 1;
            else if (c < 0)
                return -1;
            else
                return 0;
        }

        @Override
        public int compare(Grade a, Grade b) {
            if (a.getValue() > b.getValue())
                return 1;
            else if (a.getValue() < b.getValue())
                return -1;
            else
                return 0;
        }

        @Override
        public int compare(Course a, Course b) {
            int c = a.getCode().compareToIgnoreCase(b.getCode());
            
            if (c > 0)
                return 1;
            else if (c < 0)
                return -1;
            else
                return 0;
        }

        @Override
        public int compare(Homeform a, Homeform b) {
            int c = a.getCode().compareToIgnoreCase(b.getCode());
            
            if (c > 0)
                return 1;
            else if (c < 0)
                return -1;
            else
                return 0;
        }
    },
    GRADE {
        @Override
        public int compare(Student a, Student b) {
            return NAME.compare(a.getGrade(), b.getGrade());
        }

        @Override
        public int compare(Class a, Class b) {
            return NAME.compare(a.getGrade(), b.getGrade());
        }

        @Override
        public int compare(Grade a, Grade b) {
            return NAME.compare(a, b);
        }

        @Override
        public int compare(Course a, Course b) {
            return NAME.compare(a.getGrade(), b.getGrade());
        }

        @Override
        public int compare(Homeform a, Homeform b) {
            return NAME.compare(a.getGrade(), b.getGrade());
        }
    },
    CLASS {
        @Override
        public int compare(Student a, Student b) {
            throw new UnsupportedOperationException("Cannot compare students using this criteria.");
        }

        @Override
        public int compare(Class a, Class b) {
            return NAME.compare(a, b);
        }

        @Override
        public int compare(Grade a, Grade b) {
            throw new UnsupportedOperationException("Cannot compare grades using this criteria.");
        }

        @Override
        public int compare(Course a, Course b) {
            return NAME.compare(a, b);
        }

        @Override
        public int compare(Homeform a, Homeform b) {
            return NAME.compare(a.getClassroom(), b.getClassroom());
        }
    },
    HOMEFORM {
        @Override
        public int compare(Student a, Student b) {
            int c = a.getHomeform().getCode().compareToIgnoreCase(b.getHomeform().getCode());
            
            if (c > 0)
                return 1;
            else if (c < 0)
                return -1;
            else
                return 0;
        }

        @Override
        public int compare(Class a, Class b) {
            throw new UnsupportedOperationException("Cannot compare classes using this criteria.");
        }

        @Override
        public int compare(Grade a, Grade b) {
            throw new UnsupportedOperationException("Cannot compare grades using this criteria.");
        }

        @Override
        public int compare(Course a, Course b) {
            throw new UnsupportedOperationException("Cannot compare courses using this criteria.");
        }

        @Override
        public int compare(Homeform a, Homeform b) {
            return NAME.compare(a, b);
        }
    },
    COURSE {
        @Override
        public int compare(Student a, Student b) {
            throw new UnsupportedOperationException("Cannot compare students using this criteria.");
        }

        @Override
        public int compare(Class a, Class b) {
            return NAME.compare(a.getCourse(), b.getCourse());
        }

        @Override
        public int compare(Grade a, Grade b) {
            throw new UnsupportedOperationException("Cannot compare grades using this criteria.");
        }

        @Override
        public int compare(Course a, Course b) {
            return NAME.compare(a, b);
        }

        @Override
        public int compare(Homeform a, Homeform b) {
            return NAME.compare(a.getClassroom().getCourse(), b.getClassroom().getCourse());
        }
    },
    COURSE_CODE {
        @Override
        public int compare(Student a, Student b) {
            throw new UnsupportedOperationException("Cannot compare students using this criteria.");
        }

        @Override
        public int compare(Class a, Class b) {
            throw new UnsupportedOperationException("Cannot compare classes using this criteria.");
        }

        @Override
        public int compare(Grade a, Grade b) {
            throw new UnsupportedOperationException("Cannot compare grades using this criteria.");
        }

        @Override
        public int compare(Course a, Course b) {
            int c = a.getCode().compareToIgnoreCase(b.getCode());
            
            if (c > 0)
                return 1;
            else if (c < 0)
                return -1;
            else
                return 0;
        }

        @Override
        public int compare(Homeform a, Homeform b) {
            throw new UnsupportedOperationException("Cannot compare homeforms using this criteria.");
        }
    },
    STUDENT_ID {
        @Override
        public int compare(Student a, Student b) {
            if (a.getStudentId() > b.getStudentId())
                return 1;
            else if (a.getStudentId() < b.getStudentId())
                return -1;
            else
                return 0;
        }

        @Override
        public int compare(Class a, Class b) {
            throw new UnsupportedOperationException("Cannot compare classes using this criteria.");
        }

        @Override
        public int compare(Grade a, Grade b) {
            throw new UnsupportedOperationException("Cannot compare grades using this criteria.");
        }

        @Override
        public int compare(Course a, Course b) {
            throw new UnsupportedOperationException("Cannot compare courses using this criteria.");
        }

        @Override
        public int compare(Homeform a, Homeform b) {
            throw new UnsupportedOperationException("Cannot compare homeforms using this criteria.");
        }
    };
    
    /**
     * Compares two students based on a given Criteria.
     * Useful for mergeSort
     * @param a
     * @param b
     * @return 1 if a > b, 0 if a == b, or -1 if a < b, based on Criteria
     */
    public abstract int compare(Student a, Student b);

    /**
     * Compares two classes based on a given Criteria.
     * Useful for mergeSort
     * @param a
     * @param b
     * @return 1 if a > b, 0 if a == b, or -1 if a < b, based on Criteria
     */
    public abstract int compare(Class a, Class b);

    /**
     * Compares two grades based on a given Criteria.
     * Useful for mergeSort
     * @param a
     * @param b
     * @return 1 if a > b, 0 if a == b, or -1 if a < b, based on Criteria
     */
    public abstract int compare(Grade a, Grade b);

    /**
     * Compares two courses based on a given Criteria.
     * Useful for mergeSort
     * @param a
     * @param b
     * @return 1 if a > b, 0 if a == b, or -1 if a < b, based on Criteria
     */
    public abstract int compare(Course a, Course b);

    /**
     * Compares two homeforms based on a given Criteria.
     * Useful for mergeSort
     * @param a
     * @param b
     * @return 1 if a > b, 0 if a == b, or -1 if a < b, based on Criteria
     */
    public abstract int compare(Homeform a, Homeform b);
    
    @Override
    public String toString() {
        String[] split = this.name().split("_");
        String result = "";
        
        for (String s : split) {
            result += s.toUpperCase().replace(s.substring(1), s.substring(1).toLowerCase()) + " ";
        }
        
        return result.trim();
    }
    
}