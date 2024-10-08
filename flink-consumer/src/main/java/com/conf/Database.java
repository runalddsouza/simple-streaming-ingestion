package com.conf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"name", "ddl"})

public class Database {

    @JsonProperty("name")
    private String name;
    @JsonProperty("ddl")
    private String ddl;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("ddl")
    public String getDdl() {
        return ddl;
    }

    @JsonProperty("ddl")
    public void setDdl(String ddl) {
        this.ddl = ddl;
    }
}