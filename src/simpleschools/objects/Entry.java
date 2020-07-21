package simpleschools.objects;

/**
 *
 * @author Kenneth Tran
 */
public class Entry {

    private String name, date;
    private double unit;
    private double weight, mark;

    public Entry(String name, String date, double unit, double weight, double mark) {
        this.name = name;
        this.date = date;
        this.unit = unit;
        this.weight = weight;
        this.mark = mark;
    }

    public String getName() {
        return this.name;
    }

    public String getDate() {
        return this.date;
    }

    public double getUnit() {
        return this.unit;
    }

    public double getWeight() {
        return this.weight;
    }

    public double getMark() {
        return this.mark;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUnit(double unit) {
        this.unit = unit;
    }

    /**
     * Set the weight of this entry. Will default to 0 if given a number less than 0.
     * @param weight 
     */
    public void setWeight(double weight) {
        this.weight = weight < 0 ? 0 : weight;
    }

    /**
     * Set the mark of this entry. Will default to -1 (No mark; will not count towards average) if given a number less than 0.
     * @param mark 
     */
    public void setMark(double mark) {
        this.mark = mark < 0 ? -1 : mark;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
}