package sample;

import java.sql.Date;

public class Ask {

    int id;
    String text;
    String level;
    Date date;
    String subject;
    String answer;
    String author;

    public Ask(int id, String text, String level, Date date, String subject, String answer, String author) {
        this.id = id;
        this.text = text;
        this.level = level;
        this.date = date;
        this.subject = subject;
        this.answer = answer;
        this.author = author;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
