package entity;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private String guid;
	private String name;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** full constructor */
	public User(String name) {
		this.name = name;
	}

	// Property accessors

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}