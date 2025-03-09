package com.rbanking.cservice.util;

import com.rbanking.cservice.dto.LoginDTO;
import com.rbanking.cservice.entities.Login;

public class LoginWrapper {

	public static LoginDTO toLoginDto(Login login) {
		return new LoginDTO(login.getCustId(), login.getEmail(), login.getRole());

	}

}
