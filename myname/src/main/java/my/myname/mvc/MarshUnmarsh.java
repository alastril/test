package my.myname.mvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import my.myname.crud_spr_data.entity.Zoo;

public class MarshUnmarsh implements Serializable{

	private static final long serialVersionUID = -8498768420649739347L;
	
	private List<Zoo> zoo = new ArrayList<>();

	public MarshUnmarsh( List<Zoo> zoo) {
		this.zoo = zoo;
	}
	public List<Zoo> getZoo() {
		return zoo;
	}

	public void setZoo(List<Zoo> zoo) {
		this.zoo = zoo;
	}
	

}
