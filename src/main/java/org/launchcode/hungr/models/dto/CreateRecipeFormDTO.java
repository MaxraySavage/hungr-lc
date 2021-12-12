package org.launchcode.hungr.models.dto;

import org.launchcode.hungr.models.Ingredient;
import org.launchcode.hungr.models.Recipe;
import org.launchcode.hungr.models.RecipeStep;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class CreateRecipeFormDTO {


    @NotBlank(message="Name must not be blank.")
    @Size(max=100, message="Name must be less than 100 characters")
    private String name;


    @NotBlank(message="Description must not be blank")
    @Size(max=240, message="Description must be less than 240 characters")
    private String shortDescription;

    private List<String> ingredients = new ArrayList<>();

    private List<String> steps = new ArrayList<>();

    public CreateRecipeFormDTO() {
    }

    public CreateRecipeFormDTO(Recipe recipe) {
        mapFieldsFromRecipe(recipe);
    }

    public void mapFieldsFromRecipe(Recipe recipe) {
        this.name = recipe.getName();
        this.shortDescription = recipe.getShortDescription();
        this.ingredients.clear();
        for(Ingredient ingredient : recipe.getIngredients()) {
            this.ingredients.add(ingredient.getName());
        }
        this.steps.clear();
        for(RecipeStep step : recipe.getSteps()) {
            this.steps.add(step.getText());
        }
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

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
}
