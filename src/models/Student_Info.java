package models;

public class Student_Info extends Student{
    String info;

    public Student_Info(int id, String name, String lastName, String middleName, String userName, String password, String info) {
        super(id, name, lastName, middleName, userName, password);
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
