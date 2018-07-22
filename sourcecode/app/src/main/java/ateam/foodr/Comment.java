package ateam.foodr;

public class Comment {

    private String time;
    private String commenter;
    private String commentTxt;
    private  String comentId;
    public Comment() {

    }

    public Comment(String comentId, String commenter, String commentTxt, String time) {
        this.time = time;
        this.commenter = commenter;
        this.commentTxt = commentTxt;
        this.comentId = comentId;
    }

    public String getComentId() {
        return comentId;
    }

    public void setComentId(String comentId) {
        this.comentId = comentId;
    }

    public String getTime() {
        return time;
    }

    public String getCommenter() {
        return commenter;
    }

    public String getCommentTxt() {
        return commentTxt;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCommenter(String commenter) {
        this.commenter = commenter;
    }

    public void setCommentTxt(String commentTxt) {
        this.commentTxt = commentTxt;
    }
}
