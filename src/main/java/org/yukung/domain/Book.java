package org.yukung.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;
	private String description;

	@Id
	@GeneratedValue
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	@Column(nullable = false)
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	@Column(length = 1000)
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getId() + ", " + getTitle() + ", " + getDescription() + "]";
	}
}
