package teshlya.com.serotonin.model;

public class Comment {
    private String body;
    private String author;
    private String score;
    private String date;

    public String getBody() {
        return body;
    }

    public void setBody(String comment) {
        this.body = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Comment getComment(){
        return this;
    }

}
