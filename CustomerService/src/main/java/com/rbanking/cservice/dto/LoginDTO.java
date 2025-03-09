package com.rbanking.cservice.dto;

import com.rbanking.cservice.entities.Role;
import com.rbanking.cservice.validation.ValidPassword;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


public class LoginDTO {

    private Long custId;
    private String email;
    @ValidPassword
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public LoginDTO(){
        super();
    }

    public LoginDTO(Long custId, String email, String password, Role role) {
        this.custId = custId;
        this.email = email;
        this.password = password;
        this.role = role;
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
}
