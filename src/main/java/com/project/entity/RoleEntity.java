package com.project.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

@Entity()
@Table(name = "`role`")
@Getter
@Setter
@NoArgsConstructor
public class RoleEntity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column
	private String name;
	
	@ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;
	
	public RoleEntity(String name) {
		this.name = name;
	}
}