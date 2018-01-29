package my.myname;


import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Component;

import my.myname.anotations.AnotationFormater;
import my.myname.aop.SimpleBean;

@Component("simpleBeanImpl")
@Scope("prototype")
public class SimpleBeanImpl implements SimpleBean {

	private long id;
	
	@AnotationFormater(oldChar='!', newChar='.')
	private String name;
	@DateTimeFormat(pattern="dd-yyyy-MM")
	private DateTime dt;
	
	@Override
	public String sayHello(String msg) {
		String res = "Hello " + msg;
		System.out.println(res);
		return res;
	}
	
	public String sayFuck(String msg) {
		String res = "Fuck " + msg;
		System.out.println(res);
		return res;
	}

	public void say(String msg) {
		String res = "Fuck-Fuck-Fuck!!!" + msg;
		System.out.println(res);
	}

	public DateTime getDt() {
		return dt;
	}

	public void setDt(DateTime dt) {
		this.dt = dt;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(@AnotationFormater(oldChar='!',newChar='.')String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "id:"+getId()+", name:"+getName() + ", date:"+getDt();
	}
}
