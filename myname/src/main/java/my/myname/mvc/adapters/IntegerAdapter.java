package my.myname.mvc.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerAdapter extends XmlAdapter<String, Long>{

	@Override
	public String marshal(Long v) throws Exception {
		
		return String.valueOf(v);
	}

	@Override
	public Long unmarshal(String v) throws Exception {
		// TODO Auto-generated method stub
		return Long.valueOf(v);
	}

}
