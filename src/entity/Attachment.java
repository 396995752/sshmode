package entity;

import java.sql.Blob;

/**
 * Attachment entity. @author MyEclipse Persistence Tools
 */

public class Attachment implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private Blob content;
	private String url;

	// Constructors

	/** default constructor */
	public Attachment() {
	}

	/** minimal constructor */
	public Attachment(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public Attachment(Integer id, String name, Blob content, String url) {
		this.id = id;
		this.name = name;
		this.content = content;
		this.url = url;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Blob getContent() {
		return this.content;
	}

	public void setContent(Blob content) {
		this.content = content;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}