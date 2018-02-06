package my.myname.crud_spr_data.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Component
@Scope(scopeName="prototype")
@Entity
@Table(name="ZOO")
@NamedQueries({
	@NamedQuery(name="selectAll", query="select distinct z from Zoo z left join fetch z.animalsList al" ),
	@NamedQuery(name="selectById", query="select z from Zoo z left join fetch z.animalsList al where z.id = :id"),
})

public class Zoo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4669208368736285110L;
	@JacksonXmlProperty(localName="id", isAttribute=true)
	private Long id;
	@JacksonXmlProperty(localName="name")
	private String name;
	@JacksonXmlProperty(localName="dateCreation")
	private DateTime dateCreation;
	@JacksonXmlProperty(localName = "animalsList")
    @JacksonXmlElementWrapper(useWrapping = false)
	private Set<Animals> animalsList = new HashSet<Animals>();
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name="DATE")
	public DateTime getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(DateTime dateCreation) {
		this.dateCreation = dateCreation;
	}
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="zoo_animals", joinColumns=@JoinColumn(name="id_zoo"), inverseJoinColumns= @JoinColumn(name="id_animal"))
	public Set<Animals> getAnimalsList() {
		return animalsList;
	}
	public void setAnimalsList(Set<Animals> animalsList) {
		this.animalsList = animalsList;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Zoo zoo = new Zoo();
		zoo.setDateCreation(this.getDateCreation());
		zoo.setName(this.getName());
//		zoo.setAnimalsList(new HashSet<>(this.getAnimalsList()));
		return zoo;
	}
	
	@Override
	public String toString() {
		return "id:"+getId() + ", name:" + getName() + ", dateCreat:" + getDateCreation();
	}
}
