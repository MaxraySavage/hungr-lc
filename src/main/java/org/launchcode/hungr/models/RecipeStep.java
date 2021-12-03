package org.launchcode.hungr.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class RecipeStep extends AbstractEntity{

    @NotBlank(message = "Step must not be blank")
    @Size(min = 1, max = 1000, message = "Step must be less than 1000 characters")
    private String text;

    @ManyToOne
    private Recipe recipe;

    public RecipeStep(String text) {
        this.text = text;
    }

    public RecipeStep() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
