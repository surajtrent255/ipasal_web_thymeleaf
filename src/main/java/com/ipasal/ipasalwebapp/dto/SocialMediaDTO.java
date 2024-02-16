package com.ipasal.ipasalwebapp.dto;


/**
 * 
 * @author Tanchhowpa
 * Sep 5, 2019, 1:41:29 PM
 *
 */
public class SocialMediaDTO {

	private int socialMediaId;
	private String socialName;
	private String socialIcon;
	private String socialLink;
	private boolean active;
	
	
	
	public String getSocialIcon() {
		return socialIcon;
	}
	public void setSocialIcon(String socialIcon) {
		this.socialIcon = socialIcon;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public int getSocialMediaId() {
		return socialMediaId;
	}
	public void setSocialMediaId(int socialMediaId) {
		this.socialMediaId = socialMediaId;
	}
	public String getSocialName() {
		return socialName;
	}
	public void setSocialName(String socialName) {
		this.socialName = socialName;
	}
	public String getSocialLink() {
		return socialLink;
	}
	public void setSocialLink(String socialLink) {
		this.socialLink = socialLink;
	}
	
}
