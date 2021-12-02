package org.launchcode.hungr.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Ingredient extends AbstractEntity{

    @NotBlank(message = "Ingredient must not be blank")
    @Size(min = 1, max = 100, message = "Ingredient must be less than 100 characters")
    private String name;

    @ManyToOne
    private Recipe recipe;

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
