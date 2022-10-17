package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`users`")
@Getter
@Setter
public class UserEntity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "user_name",nullable = false, unique = true)
	private String userName;
	
	@Column(nullable = false)
	private String password;
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "active")
	private int active = 1; // default account is active
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntity> roles;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_exam", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "exam_id"))
	private List<ExamEntity> exams;

	@OneToMany(mappedBy = "user")
	private List<ExamResultEntity> examResults;

	/*@OneToMany(mappedBy = "user")
	private List<QuestionResultEntity> questionResults;*/
	
	public void addRole(RoleEntity role) {
		if(CollectionUtils.isEmpty(this.roles)) {
			this.roles = new ArrayList<>();
		}

        this.roles.add(role);
	}
}
