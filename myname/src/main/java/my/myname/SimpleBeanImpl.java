package my.myname;


import java.io.Serializable;

import org.joda.time.DateTime;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.datatype.joda.deser.DateTimeDeserializer;
//import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;

import my.myname.anotations.AnotationFormater;
import my.myname.aop.SimpleBean;

@Component("simpleBeanImpl")
@Scope("prototype")
public class SimpleBeanImpl implements SimpleBean, Serializable{

	private static final long serialVersionUID = 4380056633175980679L;
	private long id;
	@AnotationFormater(oldChar='!', newChar='.')
	private String name;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private DateTime dt;
	
	public SimpleBeanImpl() {
	}
	
	public SimpleBeanImpl(String name) {
		this.name=name;
	}
	
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
	
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "id:"+getId()+", name:"+getName() + ", date:"+getDt();
	}
}
