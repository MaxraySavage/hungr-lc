package org.launchcode.hungr.controllers;

import org.launchcode.hungr.data.RecipeRepository;
import org.launchcode.hungr.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("recipes")
public class RecipesController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping
    public String getHome(){
        return "recipes/index-mock";
    }

    @GetMapping("create")
    public String renderCreateRecipeForm(Model model){
        model.addAttribute("title", "Add a Recipe");
        model.addAttribute(new Recipe());
        return "recipes/create";
    }

    @PostMapping("create")
    public String processCreateRecipeForm(@ModelAttribute @Valid Recipe newRecipe, Errors errors, Model model){
        if(errors.hasErrors()){
            return "recipes/create";
        }
        recipeRepository.save(newRecipe);
        return "redirect:";
    }
}
