package my.myname.aop;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component("simpleBeanImpl")
public class SimpleBeanImpl implements SimpleBean {

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
		String res = "Fuck-Fuck-Fuck!!!";
		System.out.println(res);
	}
}
