package org.yukung.sandbox.dropwizard.sample.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Saying {
	private long id;

	@Length(max = 3)
	private String content;
}
