package org.yukung.sandbox.dropwizard.sample;

import io.dropwizard.Configuration;

import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SampleConfiguration extends Configuration {
	@NotEmpty
	private String template;

	@NotEmpty
	private String defaultName = "stranger";

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

	@JsonProperty
	public String getTemplate() {
		return template;
	}

	@JsonProperty
	public void setTemplate(String template) {
		this.template = template;
	}

	@JsonProperty
	public String getDefaultName() {
		return defaultName;
	}

	@JsonProperty
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}

    @JsonProperty("database")
    public DataSourceFactory getDatabaseFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDatabaseFactory(DataSourceFactory databaseFactory) {
        this.database = databaseFactory;
    }
}
