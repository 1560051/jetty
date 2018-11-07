package entity;

public class Article {
    public void setId(int id) {
        this.id = id;
    }

    int id;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }

    public String getContent() {
        return content;
    }

    String title;
    String tag;
    String content;

    public Article(){

    }

    public void set(int id, String title, String tag, String content){
        this.id=id;
        this.content=content;
        this.tag=tag;
        this.title=title;
    }

    public Article(String title, String tag, String content) {
        this.content=content;
        this.tag=tag;
        this.title=title;
    }

    public Article(int id, String title, String tag, String content) {
        this.id=id;
        this.content=content;
        this.tag=tag;
        this.title=title;
    }

}
