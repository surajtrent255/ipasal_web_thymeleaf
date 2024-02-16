package com.ipasal.ipasalwebapp.Controller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import com.ipasal.ipasalwebapp.Services.AboutService;
import com.ipasal.ipasalwebapp.dto.AboutDTO;
import com.ipasal.ipasalwebapp.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ipasal.ipasalwebapp.exception.CustomExceptionThrowerUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


@Controller
@RequestMapping("/contact")
public class ContactController {
	private AboutService aboutService;
	private Logger LOGGER = LoggerFactory.getLogger(ContactController.class);
	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	Configuration freeMarkerConfiguration;

	public ContactController(AboutService aboutService) {
		this.aboutService = aboutService;
	}

	@GetMapping
	String getContactUsView(Model model) {
		Response<List<AboutDTO>> aboutInfoResponse = (Response<List<AboutDTO>>) aboutService.getAboutInfo();
		if (aboutInfoResponse.getData() != null && aboutInfoResponse.getData().size() > 0) {
			AboutDTO aboutData = aboutInfoResponse.getData().get(0);
			model.addAttribute("aboutInfo", aboutData);
			return "contact";
		}
		
		throw CustomExceptionThrowerUtil.throwException(aboutInfoResponse.getStatus(), aboutInfoResponse.getMessage());
	}


	@PostMapping
	String contact(@RequestParam("name") String name, 
					@RequestParam("phone-number") String phoneNumber, 
			@RequestParam("email") String email, 
					@RequestParam("message") String msg, Model model) throws TemplateException {
		MimeMessage message = javaMailSender.createMimeMessage();
		Map<String, String> map = new HashMap<>();

		try {
			map.put("senderName", name);
			map.put("signature", "http://localhost:9966/");
			map.put("address", "Hattiban, Lalitpur");
			map.put("officeContactNo", "01-4331824");
			map.put("senderEmail", email);
			map.put("msg", msg);
			map.put("senderContactNo", phoneNumber);

			Template template = freeMarkerConfiguration.getTemplate("email.ftl");
			String htmlMsg = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			helper.setTo("info@ishanitech.com");
			helper.setFrom(email);
			helper.setSubject("Query From ContactUs Page.");
			helper.setText(htmlMsg, true);
			helper.addInline("logo.png", new ClassPathResource("static/images/logo.png"));

			javaMailSender.send(message);

			model.addAttribute("emailSent", true);

		} catch (Exception ex) {
			LOGGER.error("ERROR MSG: " + ex.getMessage());
			throw CustomExceptionThrowerUtil.throwException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong while sending email");
		}
		
		return "contact";
	}
}
