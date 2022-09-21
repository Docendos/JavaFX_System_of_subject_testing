package models;

public class Stud {
    String name;
    String last;
    int count;
    String text;

    public Stud(String name, String last, int count, String text) {
        this.name = name;
        this.last = last;
        this.count = count;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
