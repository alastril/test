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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import my.myname.mvc.adapters.IntegerAdapter;

@Entity
@Table(name="FOOD")
@Component
@Scope(scopeName="prototype")
@XmlRootElement(name="Food")@XmlSeeAlso({Animals.class})//jaxB
public class Food implements Serializable{

	private static final long serialVersionUID = 8196971690147183789L;
	private Long id;
	private String name;
	private Animals animals;
	
	@XmlID @XmlJavaTypeAdapter(value = IntegerAdapter.class, type = String.class)//jaxB
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
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

	
	@XmlIDREF//jaxB
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
