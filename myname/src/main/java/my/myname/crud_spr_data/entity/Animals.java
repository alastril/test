package my.myname.crud_spr_data.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
@Entity
@Table(name="ANIMALS")
@Component
@Scope(scopeName="prototype")

public class Animals implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1716262289447197232L;
	@JacksonXmlProperty(localName="id", isAttribute=true)
	private Long id;
	@JacksonXmlProperty(localName="name")
	private String name;
	@JacksonXmlProperty(localName="zooList")
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<Zoo> zooList = new HashSet<Zoo>();
	@JacksonXmlProperty(localName="foodList")
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<Food> foodList = new ArrayList<Food>();
	
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
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(
			name="zoo_animals",
			joinColumns = @JoinColumn(name="id_animal"),
			inverseJoinColumns = @JoinColumn(name = "id_zoo"))
	public Set<Zoo> getZooList() {
		return zooList;
	}
	public void setZooList(Set<Zoo> zooList) {
		this.zooList = zooList;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Animals an = new Animals();
		an.setName(this.getName());
//		an.setZooList(this.getZooList());
//		an.setFoodList(this.getFoodList());
		return an;
	}
	
	@Override
	public String toString() {
		return "id:"+getId() + ", name:" + getName();
	}
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="animals",  orphanRemoval=true, fetch=FetchType.EAGER)
	public List<Food> getFoodList() {
		return foodList;
	}
	public void setFoodList(List<Food> foodList) {
		this.foodList = foodList;
	}

	public void addFood(Food food){
		food.setAnimals(this);
		getFoodList().add(food);
	}
	
	public void removeFood(Food food){
		getFoodList().remove(food);
	}
}
