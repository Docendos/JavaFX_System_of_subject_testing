package models;

import java.sql.Date;

public class TestAsks {
    String name;
    String last;
    int mark;
    Date date;

    public TestAsks(String name, String last, int mark, Date date) {
        this.name = name;
        this.last = last;
        this.mark = mark;
        this.date = date;
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

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
