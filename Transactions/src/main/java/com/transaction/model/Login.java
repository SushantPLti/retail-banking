package com.transaction.model;

import jakarta.persistence.*;


@Entity
public class Login {

    @Id
    private Long custId;

    private String email;

    // @ValidPassword(message = "Password must be at least 8 character long and
    // contains at least "
    // + "one digit, one lower case, one upper case and no whitespace")
    // @NotNull(message = "Please provide password!!")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isLoggedOut;

    public Login(){
        super();
    }

    public Login(Long custId, String email, String password, Role role) {
        this.custId = custId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isLoggedOut() {
        return isLoggedOut;
    }

    public void setLoggedOut(boolean loggedOut) {
        isLoggedOut = loggedOut;
    }
}
