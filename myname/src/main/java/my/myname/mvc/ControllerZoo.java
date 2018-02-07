package my.myname.mvc;


import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.Module.SetupContext;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import my.myname.crud_spr_data.entity.Animals;
import my.myname.crud_spr_data.entity.Food;
import my.myname.crud_spr_data.entity.Zoo;
import my.myname.crud_spr_data.interfaces.ZooDao;

@Controller
@RequestMapping(value="/zoo")
public class ControllerZoo {
	private static final Logger log = Logger.getLogger(ControllerZoo.class);
	@Autowired
	private ZooDao zooDao;
	@Autowired
	private MarshUnmarsh rootDocument;



	@RequestMapping(value="/xmlzoo", method=RequestMethod.GET, produces=MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody MarshUnmarsh getXmlZoo() throws Throwable{
		try {
	        rootDocument.setZooList(zooDao.getListZoo());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return rootDocument;
	}
	
	@RequestMapping(value="/xmlzoo", method=RequestMethod.POST, produces=MediaType.APPLICATION_XML_VALUE)
	@ResponseStatus(code=HttpStatus.CREATED)
	public void setXmlZoo(@RequestBody MarshUnmarsh marshUnmarsh) throws Throwable{
		try {
			 marshUnmarsh.getZooList().stream().forEach(action -> {zooDao.save(action);});
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
}
