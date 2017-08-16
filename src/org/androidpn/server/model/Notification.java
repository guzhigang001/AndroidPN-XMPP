package org.androidpn.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用于存储离线消息数据保存的表
 * @author gag
 *
 */

@Entity//表示此类为实体类并与数据的表的映射
@Table(name = "notification")
public class Notification {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name = "api_key", length = 64)
	private String apiKey;
	
	@Column(name = "username", nullable = false, length = 64)
	private String username;
	
	@Column(name = "title", nullable = false, length = 64)
	private String title;
	
	@Column(name = "message", nullable = false, length = 1000)
	private String message;
	 
	@Column(name = "uri", length = 256)
	private String uri;
    
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Column(name="uuid", length=64, unique=true, nullable=false)
	private String uuid;
	
	@Column(name="image_url", length=64)
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

}
