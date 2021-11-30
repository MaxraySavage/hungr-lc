package org.launchcode.hungr.controllers;

import org.launchcode.hungr.data.RecipeRepository;
import org.launchcode.hungr.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("recipes")
public class RecipesController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping
    public String displayRecipes(Model model){
        model.addAttribute("title", "All Recipes");
        model.addAttribute("recipes", recipeRepository.findAll());
        return "recipes/index";
    }

    @GetMapping("/details/{recipeId}")
    public String displayRecipe(Model model, @PathVariable int recipeId){
        Optional<Recipe> optionRecipe = recipeRepository.findById(recipeId);
        if(optionRecipe.isEmpty()){
            // TODO: Add a `recipe not found` page
            return "redirect:/recipes";
        }
        Recipe recipe = optionRecipe.get();
        model.addAttribute("title", "Recipe Details");
        model.addAttribute("recipe", recipe);
        return "recipes/details";
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

    @GetMapping("/edit/{recipeId}")
    public String renderEditRecipeForm(Model model, @PathVariable int recipeId){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()) {
            return "redirect:";
        }
        Recipe recipe = optionalRecipe.get();
        model.addAttribute("title", "Edit Recipe");
        model.addAttribute("recipe", recipe);

        return "recipes/edit";
    }

    @PostMapping("/edit/{recipeId}")
    public String renderEditRecipeForm(Model model, @PathVariable int recipeId, @ModelAttribute @Valid Recipe updatedRecipe, Errors errors){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()) {
            return "redirect:";
        }
        Recipe recipe = optionalRecipe.get();
        if(errors.hasErrors()){
            model.addAttribute("title", "Edit Recipe");
            model.addAttribute("recipe", updatedRecipe);
            return "recipes/edit";
        }
        recipeRepository.save(updatedRecipe);
        return "redirect:recipes/details/" + recipeId;
    }

    @PostMapping("delete")
    public String processDeleteRecipe(@RequestParam(required = false) Integer recipeId){
        if(recipeId == null){
            return "redirect:";
        }
        recipeRepository.deleteById(recipeId);
        return "redirect:";
    }
}
