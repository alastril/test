package my.myname.formaters;

import java.text.ParseException;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.format.Formatter;

import my.myname.SimpleBeanImpl;
import my.myname.anotations.AnotationFormater;


/**
 * The Class FormaterForAnotation.
 * Formatter for replacing chars in {@link}String fields.
 * Using in {@link}TestAnotationFormatter class
 */
public class FormaterForAnotation implements Formatter<String>// return type for
																// field where
																// annotation
{

	/** The old char. */
	private Character oldChar;
	
	/** The new char. */
	private  Character newChar;

	/**
	 * Gets the old char.
	 *
	 * @return the old char
	 */
	public Character getOldChar() {
		return oldChar;
	}

	/**
	 * Sets the old char.
	 *
	 * @param oldChar the new old char
	 */
	public void setOldChar(Character oldChar) {
		this.oldChar = oldChar;
	}

	/**
	 * Gets the new char.
	 *
	 * @return the new char
	 */
	public Character getNewChar() {
		return newChar;
	}

	/**
	 * Sets the new char.
	 *
	 * @param newChar the new new char
	 */
	public void setNewChar(Character newChar) {
		this.newChar = newChar;
	}

	/* (non-Javadoc)
	 * @see org.springframework.format.Printer#print(java.lang.Object, java.util.Locale)
	 */
	@Override
	public String print(String object, Locale locale) {
		//TODO AOP LOG
		return object;
	}

	/* (non-Javadoc)
	 * @see org.springframework.format.Parser#parse(java.lang.String, java.util.Locale)
	 */
	@Override
	public String parse(String text, Locale locale) throws ParseException {
		//TODO AOP LOG
		System.out.println("Parse with Anotation:" + text);
		return text.replace(oldChar, newChar);
	}

}
