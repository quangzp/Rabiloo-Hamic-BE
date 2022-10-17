package com.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends AbstractDto{
	private String userName;
	private String password;
	private String lastName;
	private String fullName;
}
