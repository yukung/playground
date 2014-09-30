package org.yukung.sandbox.dropwizard.sample.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author yukung
 */
@Entity
@Table(name = "people")
@NamedQueries({ @NamedQuery(name = "org.yukung.sandbox.dropwizard.sample.core.Person.findAll", query = "SELECT p FROM Person p") })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "fullName", nullable = false)
	private String fullName;

	@Column(name = "jobTitle", nullable = false)
	private String jobTitle;
}
