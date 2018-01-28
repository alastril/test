package my.myname.converters;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


public class StringToDateConverter implements Converter<String, DateTime>{
	
	private DateTimeFormatter dateFomater;
	private String datePattern = "dd-MM-yyyy";
	
	@PostConstruct
	public void init(){
		dateFomater=DateTimeFormat.forPattern(datePattern);
	} 
	
	@Override
	public DateTime convert(String strinigDate) {
		return dateFomater.parseDateTime(strinigDate);
	}

}
