package org.launchcode.hungr.controllers;

import org.launchcode.hungr.data.RecipeRepository;
import org.launchcode.hungr.models.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("search")
public class SearchController {

    @Autowired
    private RecipeRepository recipeRepository;

    @GetMapping
    public String getSearchHome(Model model, @RequestParam(required = false) String q){
        Iterable<Recipe> recipes;
        if(q == null){
            recipes = recipeRepository.findAll();
        } else {
            recipes = recipeRepository.findByNameContainingIgnoreCase(q);
        }
        String title = "Search for: \"" + q + "\"";

        model.addAttribute("title", "Search for " + q);
        model.addAttribute("subtitle", "Don't see what you're looking for?");
        model.addAttribute("recipes", recipes);
        return "recipes/index";
    }
}
