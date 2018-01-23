package my.myname;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class MyNameConf {

	@Bean("dfs")
	public Zoo getMyName(){
		return new Zoo();
	}
	
}
