package com.rbanking.authserver.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


public class LoginDTO {

    private Long custId;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    public LoginDTO(){
        super();
    }

    public LoginDTO(Long custId, String email, Role role) {
        this.custId = custId;
        this.email = email;
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

   
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
