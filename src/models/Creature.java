package models;

public class Creature {
    String name;
    String last;
    int count;


    public Creature(String name, String last, int count) {
        this.name = name;
        this.last = last;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
