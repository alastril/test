package my.myname.mvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import my.myname.crud_spr_data.entity.Zoo;


/**
 * The Class MarshUnmarsh.
 * This class is wrapper for List(s) because jaxb not working with lists DIRECTLY(List.class or ArrayList.class etc.)
 */
@XmlRootElement(name="Document")
@Component
@Scope(scopeName="prototype")
public class MarshUnmarsh implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8498768420649739347L;

	/** The zoo list. */
	List<Zoo> zooList = new ArrayList<>();

	/**
	 * Gets the zoo list.
	 *
	 * @return the zoo list
	 */
	
	@XmlElement(name="Zoo")
	@XmlElementWrapper(name="ZooList")
	public List<Zoo> getZooList() {
		return zooList;
	}

	/**
	 * Sets the zoo list.
	 *
	 * @param zooList the new zoo list
	 */
	public void setZooList(List<Zoo> zooList) {
		this.zooList = zooList;
	}
	
	@Override
	public String toString() {
		return getZooList().toString();
	}
}
