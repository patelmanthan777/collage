package pl.collage.util.models;

public class User {

    public String fullName;
    public String email;
    public String uid;
    public String albumStorageId;
    public boolean sendingStarted;
    public boolean sendingFinished;

    @SuppressWarnings("unused")
    public User() {
        // default constructor for Firebase
    }

    public User(String fullName, String email, String uid) {
        this.fullName = fullName;
        this.email = email;
        this.uid = uid;
    }
}
