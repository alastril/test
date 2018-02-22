package my.myname.mvc;




import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
	@Autowired
	SimpMessagingTemplate template;
//	@Autowired
//	 st;


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
	public @ResponseBody MarshUnmarsh setJsonZoo(@RequestBody MarshUnmarsh marshUnmarsh) throws Throwable{
		try {  
			 marshUnmarsh.getZooList().stream().forEach(action -> {zooDao.save(action);});
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return marshUnmarsh;
	}

	
	
////WebSocket Test requests!!!!!1111
	/**
	 * Send message from rest request to websocket
	 * @param message
	 * @throws Throwable
	 */
	@RequestMapping(value= {"/sendWebSocketMess"},method=RequestMethod.POST, consumes= {MediaType.TEXT_PLAIN_VALUE})
	public void webSocketSend(@RequestBody String message) throws Throwable{
		try {    
			 
			System.out.println("Rest send to websocket: "+message);
			template.convertAndSend("/topic/pointToSend", message);// "/topic" because SimpMessagingTemplate.class don't know about default broker
		
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
	}
	
	@MessageMapping("/pointToSend")
//	@SendTo("/topic/pointToSend")
	public String webSocketGet(String message) throws Throwable{
		try {
			System.out.println("webSocketGet: "+message);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return message.toUpperCase();
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    return new ResponseEntity<String>(ex.getMessage(), headers, HttpStatus.BAD_REQUEST);
	}
}
