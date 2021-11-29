package org.launchcode.hungr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("recipes")
public class RecipesController {

    @GetMapping
    public String getHome(){
        return "recipes/index-mock";
    }

    @GetMapping("create")
    public String renderCreateRecipeForm(){
        return "recipes/create-mock";
    }
}
