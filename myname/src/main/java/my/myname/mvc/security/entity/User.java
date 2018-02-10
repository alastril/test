package my.myname.mvc.security.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Entity
@Scope(scopeName="prototype")
@NamedQueries({
	@NamedQuery(name="GetUserByName", query="select u from User u inner join u.userRoles ur where u.userName= :userName")
	})
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3963027008031611010L;
	private Long id;
	private String userName;
	private String passWord;
	private Boolean enable=true;
	private List<UserRole> userRoles = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="USER_NAME")
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="ENABLE")
	public Boolean getEnable() {
		return enable;
	}
	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="USERS_ROLES", joinColumns= @JoinColumn(name="id_user"), inverseJoinColumns=@JoinColumn(name="id_role"))
	public List<UserRole> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
	@Column(name="PASSWORD")
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	@Transient
	public String[] getRolesNames(){
		return (String[]) userRoles.stream().map(mapper -> {
			return mapper.getRoleName();}).collect(Collectors.toList()).toArray(new String[getUserRoles().size()]);
	}
	
	@Override
	public String toString() {
		return "Id:"+getId() + ", UserName:" + getUserName() + ", Enable:" + getEnable() + "\n UserRoles:" + getUserRoles().toString();
	}
}
