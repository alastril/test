package my.myname.crud_spr_data.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
@Entity
@Table(name="FOOD")
@Component
@Scope(scopeName="prototype")

public class Food implements Serializable{

	private static final long serialVersionUID = 8196971690147183789L;
	@JacksonXmlProperty(localName="id", isAttribute=true)
	private Long id;
	@JacksonXmlProperty(localName="name")
	private String name;
	@JacksonXmlProperty(localName="animals")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Animals animals;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
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

	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="ID_ANIMALS")
	public Animals getAnimals() {
		return animals;
	}
	public void setAnimals(Animals animals) {
		this.animals = animals;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		Food an = new Food();
		an.setName(this.getName());
		an.setAnimals((Animals)this.getAnimals().clone());
		return an;
	}
	
	@Override
	public String toString() {
		return "id:"+getId() + ", name:" + getName();
	}

}
