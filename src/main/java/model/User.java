package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users", schema = "webchat")
public class User {

    @Id
    @Column(name = "login", columnDefinition = "char(20)")
    private String login;

    @Column(name = "password", columnDefinition = "char(20)")
    private String password;

    @Column(name = "email", columnDefinition = "char(40")
    private String email;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
