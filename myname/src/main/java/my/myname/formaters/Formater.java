package my.myname.formaters;

import java.text.ParseException;
import java.util.Locale;

import org.joda.time.format.DateTimeFormat;
import org.springframework.format.Formatter;

import my.myname.SimpleBeanImpl;

/**
 * The Class Formater.
 * Convert from String Date(dd-yyyy-MM) to {@link SimpleBeanImpl}
 */
public class Formater implements Formatter<SimpleBeanImpl>{
	
	/** The mask. */
	private String mask = "dd-MM-yyyy"; 
	
    /**
     * Sets the mask.
     *
     * @param mask the new mask
     */
    public void setMask(String mask) {
		this.mask = mask;
	}
	
	/**
	 * Gets the mask.
	 *
	 * @return the mask
	 */
	public String getMask() {
		return mask;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.format.Printer#print(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String print(SimpleBeanImpl object, Locale locale) {
		System.out.println("Formater print:" + object.toString());
		return object.toString();
	}

	/* (non-Javadoc)
	 * @see org.springframework.format.Parser#parse(java.lang.String, java.util.Locale)
	 */
	@Override
	public SimpleBeanImpl parse(String text, Locale locale) throws ParseException {
		SimpleBeanImpl sf = new SimpleBeanImpl();
		sf.setDt(DateTimeFormat.forPattern(mask).parseDateTime(text)); 
		sf.setName("Babai say: BOO-BOO!!!");
		return sf;
	}

}
