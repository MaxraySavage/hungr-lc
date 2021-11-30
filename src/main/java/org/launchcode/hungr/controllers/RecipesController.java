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
        Recipe savedRecipe = recipeRepository.save(newRecipe);
        return "redirect:/recipes/details/" + savedRecipe.getId();
    }

    @GetMapping("/edit/{recipeId}")
    public String renderEditRecipeForm(Model model, @PathVariable int recipeId){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()) {
            return "redirect:/recipes";
        }
        Recipe recipe = optionalRecipe.get();
        model.addAttribute("title", "Edit Recipe");
        model.addAttribute("recipe", recipe);

        return "recipes/edit";
    }

    @PostMapping("/edit/{recipeId}")
    public String renderEditRecipeForm(Model model, @PathVariable int recipeId, @ModelAttribute @Valid Recipe editedRecipe, Errors errors){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()) {
            return "redirect:/recipes";
        }
        if(errors.hasErrors()){
            model.addAttribute("title", "Edit Recipe");
            model.addAttribute("recipe", editedRecipe);
            return "recipes/edit";
        }
        System.out.println(editedRecipe.getId());
        Recipe originalRecipe = optionalRecipe.get();
        originalRecipe.setName(editedRecipe.getName());
        originalRecipe.setShortDescription(editedRecipe.getShortDescription());
        recipeRepository.save(originalRecipe);
        return "redirect:/recipes/details/" + originalRecipe.getId();
    }

    @PostMapping("delete")
    public String processDeleteRecipe(@RequestParam(required = false) Integer recipeId){
        if(recipeId == null){
            return "redirect:/recipes";
        }
        recipeRepository.deleteById(recipeId);
        return "redirect:/recipes";
    }
}
