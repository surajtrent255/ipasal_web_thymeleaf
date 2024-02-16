/**
 * 
 */
package com.ipasal.ipasalwebapp.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ipasal.ipasalwebapp.Services.ToolBarMessageService;
import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ToolBarMessageDTO;



/**
 * @author Pujan K.C. <pujanov69@gmail.com>
 *
 * Created on Sep 6, 2019
 */
@Controller
@RequestMapping("/toolbarMessage")
public class ToolBarController {
	
	private ToolBarMessageService toolBarMessageService;
	
	
	 Logger logger = LoggerFactory.getLogger(ToolBarController.class);;
	
	@Autowired
	public ToolBarController(ToolBarMessageService toolBarMessageService) {
		this.toolBarMessageService = toolBarMessageService;
	}
	
	  @RequestMapping(method = RequestMethod.GET)
	    public @ResponseBody Response<?> getToolBarMessage() {
	    	Response<?> response = toolBarMessageService.getToolBarMessage();
	    	return response;
	    } 

	  @RequestMapping(method = RequestMethod.PUT)
	  public @ResponseBody Response<?> updateToolBarMessage(@RequestBody ToolBarMessageDTO toolBarMessageDTO){
		  Response<?> response = toolBarMessageService.updateToolBarMessage(toolBarMessageDTO);
		  return response;
	  }
}
