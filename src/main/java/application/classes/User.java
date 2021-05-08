package application.classes;

public class User {
    private int id;
    private String userName;
    private String lastName;
    private String email;
    private String hash;

    public User(int id, String Username, String email, String hash) {
        this.id = id;
        this.userName = Username;
        this.email = email;
        this.hash = hash;
    }

    public int getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%d) %s", userName, lastName, id, email);
    }
}
