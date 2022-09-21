package models;

public class User {
    int id;
    private String name;
    private String lastName;
    private String middleName;
    private String userName;
    private String password;



    public User(int id, String name, String lastName, String middleName,  String userName, String password) {
        this.id = id;
        this.name=name;
        this.lastName=lastName;
        this.middleName=middleName;
        this.userName = userName;
        this.password = password;
    }



    public int getId() {
        return id;
    }

    public void setId(int name) {
        this.id = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String patronymic) {
        this.middleName = patronymic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
