package org.launchcode.hungr.models;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Recipe extends AbstractEntity{

    @NotBlank(message="Name must not be blank.")
    @Size(max=100, message="Name must be less than 100 characters")
    private String name;

    @NotBlank(message="Description must not be blank")
    @Size(max=480, message="Description must be less than 480 characters")
    private String shortDescription;

    public Recipe() {}

    public Recipe(String name, String shortDescription) {
        this.name = name;
        this.shortDescription = shortDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
