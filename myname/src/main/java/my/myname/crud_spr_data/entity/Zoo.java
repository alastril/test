package my.myname.crud_spr_data.entity;


import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import my.myname.mvc.adapters.DateTimeAdapter;
import my.myname.mvc.adapters.IntegerAdapter;

@Component
@Scope(scopeName="prototype")
@Entity
@Table(name="ZOO")
@NamedQueries({
	@NamedQuery(name="selectAll", query="select distinct z from Zoo z left join fetch z.animalsList al" ),
	@NamedQuery(name="selectById", query="select z from Zoo z left join fetch z.animalsList al where z.id = :id"),
})
@XmlRootElement(name="Zoo") @XmlSeeAlso({Animals.class})//jaxB
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Zoo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4669208368736285110L;
	
	private Long id;

	private String name;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private DateTime dateCreation;
	private Set<Animals> animalsList = new HashSet<Animals>();
	
	

	@XmlID @XmlJavaTypeAdapter(value = IntegerAdapter.class, type = String.class)//jaxB
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name="ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@XmlElement//jaxB
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlElement @XmlJavaTypeAdapter(value=DateTimeAdapter.class, type=String.class)//jaxB
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name="DATE")
	public DateTime getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(DateTime dateCreation) {
		this.dateCreation = dateCreation;
	}
	
	@XmlElement(name="animal") @XmlElementWrapper(name="animalsList")//jaxB
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
		return "id:"+getId() + ", name:" + getName() + ", dateCreat:" + getDateCreation() + "\n Animal:" + getAnimalsList().toString();
	}
}
