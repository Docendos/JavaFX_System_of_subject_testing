package models;

public class NewCreature extends Creature{
    int mark;

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public NewCreature(String name, String last, int count, int mark) {
        super(name, last, count);
        this.mark = mark;
    }
}
