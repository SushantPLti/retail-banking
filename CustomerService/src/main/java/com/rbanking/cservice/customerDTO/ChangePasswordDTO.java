package com.rbanking.cservice.customerDTO;

import com.rbanking.cservice.validation.ValidPassword;

public class ChangePasswordDTO {
    private Long custId;
    @ValidPassword
    private String currentPassword;
    @ValidPassword
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }
}
