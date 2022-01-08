package org.launchcode.hungr.models.dto;

import org.launchcode.hungr.models.Ingredient;
import org.launchcode.hungr.models.Recipe;
import org.launchcode.hungr.models.RecipeStep;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class EditRecipeFormDTO extends CreateRecipeFormDTO{
    private int id;

    public EditRecipeFormDTO() {
        this.getSteps().add("");
    }

    public EditRecipeFormDTO(Recipe recipe) {
        super(recipe);
        this.id = recipe.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
