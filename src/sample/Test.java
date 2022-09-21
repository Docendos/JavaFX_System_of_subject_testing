package sample;

public class Test {
    int id;
    String text;
    String thema;

    public Test(int id, String text, String thema) {
        this.id = id;
        this.text = text;
        this.thema = thema;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getThema() {
        return thema;
    }

    public void setThema(String thema) {
        this.thema = thema;
    }
}
