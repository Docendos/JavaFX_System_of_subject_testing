package models;

import java.sql.Date;

public class New_Creature {
    String name;
    String last;
    Date dat;

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

    public Date getDat() {
        return dat;
    }

    public void setDat(Date dat) {
        this.dat = dat;
    }



    public New_Creature(String name, String last, Date dat) {
        this.name = name;
        this.last = last;
        this.dat = dat;
    }


}
