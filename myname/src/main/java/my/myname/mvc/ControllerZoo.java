package my.myname.mvc;


import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import my.myname.SimpleBeanImpl;
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
	 
   
	@RequestMapping(value="/xmlzoo", method=RequestMethod.POST, consumes =  MediaType.APPLICATION_XML_VALUE)
	@ResponseStatus(code=HttpStatus.CREATED)
	public void setXmlZoo(@RequestBody MarshUnmarsh marshUnmarsh) throws Throwable{
		try {  
			 marshUnmarsh.getZooList().stream().forEach(action -> {zooDao.save(action);});
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	@RequestMapping(value="/jsonzoo", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody MarshUnmarsh getJsonZoo() throws Throwable{
		try {
	        rootDocument.setZooList(zooDao.getListZoo());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return rootDocument;
	}
	 
	@RequestMapping(value="/jsonzoo", method=RequestMethod.POST, consumes =  MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code=HttpStatus.CREATED)
	public void setJsonZoo(@RequestBody MarshUnmarsh marshUnmarsh) throws Throwable{
		try {  
			 marshUnmarsh.getZooList().stream().forEach(action -> {zooDao.save(action);});
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    return new ResponseEntity<String>(ex.getMessage(), headers, HttpStatus.BAD_REQUEST);
	}
}
