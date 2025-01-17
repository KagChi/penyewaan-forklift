package my.id.kagchi.core;

public class Session {
    private int id;
    private String username;
    private String role;

    public Session setId(int id) {
        this.id = id;

        return this;
    }

    public Session setUsername(String username) {
        this.username = username;

        return this;
    }

    public Session setRole(String role) {
        this.role = role;

        return this;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
