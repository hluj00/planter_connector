package application.classes;

public class User {
    private int id;
    private String userName;
    private String lastName;
    private String email;

    public User(int id, String Username, String email) {
        this.id = id;
        this.userName = Username;
        this.email = email;
    }

    public int getId() {
        return id;
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
