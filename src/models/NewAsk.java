package models;

public class NewAsk {
    int id;
    String text;
    String level;

    public NewAsk(int id, String text, String level) {
        this.id = id;
        this.text = text;
        this.level = level;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
