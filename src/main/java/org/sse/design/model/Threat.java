package org.sse.design.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "threat")
@JsonRootName("threat")
public class Threat {

    @JsonIgnore
    private String id;

    @JacksonXmlProperty(localName = "name")
    @JsonProperty("name")
    private String name;

    @JsonIgnore
    private String threatCategoryName;

    @JacksonXmlProperty(localName = "description")
    @JsonProperty("description")
    private String description;

    @JsonIgnore
    private String shortDescription;

    @JsonIgnore
    private String element;

    @JacksonXmlProperty(localName = "priority")
    @JsonProperty("priority")
    private String priority;

    @JsonIgnore
    private String interactionName;

    public Threat(){}

    /* getters & setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThreatCategoryName() {
        return threatCategoryName;
    }

    public void setThreatCategoryName(String threatCategoryName) {
        this.threatCategoryName = threatCategoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getInteractionName() {
        return interactionName;
    }

    public void setInteractionName(String interactionName) {
        this.interactionName = interactionName;
    }
}
