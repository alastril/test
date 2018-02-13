package my.myname.mvc.security.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;

@Entity
@Scope(scopeName="prototype")
@Table(name="USER_ROLE")
public class UserRole implements Serializable{
	
	private static final long serialVersionUID = -7103879527059729359L;
	private Long id;
	private String roleName;
	private List<User> listUsers = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name="USERS_ROLES", joinColumns= @JoinColumn(name="id_role"), inverseJoinColumns=@JoinColumn(name="id_user"))
	public List<User> getListUsers() {
		return listUsers;
	}
	public void setListUsers(List<User> listUsers) {
		this.listUsers = listUsers;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  "id:"+getId() + ", RoleName:" + getRoleName() ;
	}
	
}
