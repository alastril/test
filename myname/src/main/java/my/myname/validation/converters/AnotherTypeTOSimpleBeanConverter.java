package my.myname.validation.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import my.myname.SimpleBeanImpl;

@Component
public class AnotherTypeTOSimpleBeanConverter implements Converter<AnotherTypeForConvert, SimpleBeanImpl>{
	
	@Autowired
	@Qualifier("simpleBeanImpl")
	SimpleBeanImpl sImpl;
	
	@Override
	public SimpleBeanImpl convert(AnotherTypeForConvert aConvert) {
		sImpl.setId(aConvert.getId());
		sImpl.setName(aConvert.getName());
		sImpl.setDt(aConvert.getDateTime());
		return sImpl;
	}

}
