package id.ac.id.telkomuniversity.tass.blogapp.Models;

import com.google.firebase.database.ServerValue;

public class CommentPost {

    String postKey, CommentKey;
    String isiComment, idUser, imageUser, userName;
    Object timestamp;

    public CommentPost() {
    }

    public CommentPost(String postKey, String isiComment, String idUser, String imageUser, String userName) {
        this.postKey = postKey;
        this.isiComment = isiComment;
        this.idUser = idUser;
        this.imageUser = imageUser;
        this.userName = userName;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public CommentPost(String isiComment, String idUser, String imageUser, String userName, Object timestamp) {
        this.isiComment = isiComment;
        this.idUser = idUser;
        this.imageUser = imageUser;
        this.userName = userName;
        this.timestamp = timestamp;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getCommentKey() {
        return CommentKey;
    }

    public void setCommentKey(String commentKey) {
        CommentKey = commentKey;
    }

    public String getIsiComment() {
        return isiComment;
    }

    public void setIsiComment(String isiComment) {
        this.isiComment = isiComment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
