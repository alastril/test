package my.myname.converters;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


/**
 * The Class StringToDateConverter.
 * Convert {@link String} to {@link DateTime}
 */
public class StringToDateConverter implements Converter<String, DateTime>{
	
	/** The date fomater. */
	private DateTimeFormatter dateFomater;
	
	/** The date pattern. */
	private String datePattern = "dd-MM-yyyy";
	
	/**
	 * Inits the.
	 */
	@PostConstruct
	public void init(){
		dateFomater=DateTimeFormat.forPattern(datePattern);
	} 
	
	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public DateTime convert(String strinigDate) {
		System.out.println("converting...."+strinigDate);
		return dateFomater.parseDateTime(strinigDate);
	}

}
