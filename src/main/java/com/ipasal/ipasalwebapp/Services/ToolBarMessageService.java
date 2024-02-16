/**
 * 
 */
package com.ipasal.ipasalwebapp.Services;

import com.ipasal.ipasalwebapp.dto.Response;
import com.ipasal.ipasalwebapp.dto.ToolBarMessageDTO;

/**
 * @author Pujan K.C. <pujanov69@gmail.com>
 *
 * Created on Sep 6, 2019
 */
public interface ToolBarMessageService {

	Response<?> getToolBarMessage();
	
	Response<?> updateToolBarMessage(ToolBarMessageDTO toolBarMessageDTO);
}
