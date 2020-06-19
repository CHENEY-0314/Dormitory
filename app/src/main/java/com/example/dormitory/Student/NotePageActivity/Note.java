package com.example.dormitory.Student.NotePageActivity;

public class Note {
    private int image;

    private String type;

    private String content;//内容

    private int id;//该条数据的id

    private String pushtime;

    private String topic;

    public int getImage(){
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public  void setType(String type){
        this.type=type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPushtime(String pushtime) {
        this.pushtime = pushtime;
    }

    public String getPushtime() {
        return pushtime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}

