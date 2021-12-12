package org.launchcode.hungr.controllers;

import org.launchcode.hungr.data.IngredientRepository;
import org.launchcode.hungr.data.RecipeRepository;
import org.launchcode.hungr.data.RecipeStepRepository;
import org.launchcode.hungr.models.Ingredient;
import org.launchcode.hungr.models.Recipe;
import org.launchcode.hungr.models.RecipeStep;
import org.launchcode.hungr.models.User;
import org.launchcode.hungr.models.dto.CreateRecipeFormDTO;
import org.launchcode.hungr.models.dto.EditRecipeFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("recipes")
public class RecipesController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;

    @ModelAttribute
    public void addUserAttributes(Model model, HttpServletRequest request) {
        Object userObj = request.getAttribute("user");
        if(userObj != null && userObj instanceof User){
            model.addAttribute("user", userObj);
        }
    }

    @GetMapping
    public String displayRecipes(Model model){
        model.addAttribute("title", "All Recipes");
        model.addAttribute("subtitle", "A list of all public recipes currently in the database.");
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
        boolean userIsAuthor = recipe.getAuthor().equals(model.getAttribute("user"));
        boolean recipeIsFavorite = ((User) model.getAttribute("user")).getFavoriteRecipes().contains(recipe);

        model.addAttribute("recipeIsFavorite", recipeIsFavorite);
        model.addAttribute("userIsAuthor",userIsAuthor);
        model.addAttribute("title", "Recipe Details");
        model.addAttribute("recipe", recipe);
        return "recipes/details";
    }

    @GetMapping("create")
    public String renderCreateRecipeForm(Model model){
        model.addAttribute("title", "Add a Recipe");
        model.addAttribute("createRecipeFormDTO", new CreateRecipeFormDTO());
        return "recipes/create";
    }

    @PostMapping("create")
    public String processCreateRecipeForm( Model model, @ModelAttribute @Valid CreateRecipeFormDTO createRecipeFormDTO, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("title", "Add a Recipe");
            return "recipes/create";
        }
        Recipe newRecipe = new Recipe(createRecipeFormDTO.getName(), createRecipeFormDTO.getShortDescription());
        // TODO: this next line may need work? feels weird to do an inline cast like this
        // Make a method that does this?
        newRecipe.setAuthor((User) model.getAttribute("user"));
        Recipe savedRecipe = recipeRepository.save(newRecipe);
        for( String ingredientName : createRecipeFormDTO.getIngredients()) {
            Ingredient newIngredient = new Ingredient(ingredientName);
            newIngredient.setRecipe(savedRecipe);
            ingredientRepository.save(newIngredient);
        }
        for( String stepText : createRecipeFormDTO.getSteps()) {
            RecipeStep newStep = new RecipeStep(stepText);
            newStep.setRecipe(savedRecipe);
            recipeStepRepository.save(newStep);
        }
        return "redirect:/recipes/details/" + savedRecipe.getId();
    }

    @GetMapping("/edit/{recipeId}")
    public String renderEditRecipeForm(Model model, @PathVariable int recipeId){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()) {
            // recipe not found in database
            return "redirect:/recipes";
        }
        Recipe recipe = optionalRecipe.get();
        boolean userIsAuthor = recipe.getAuthor().equals(model.getAttribute("user"));
        if(! userIsAuthor) {
            return "redirect:/recipes";
        }

        EditRecipeFormDTO editRecipeFormDTO = new EditRecipeFormDTO(recipe);
        model.addAttribute("title", "Edit Recipe");
        model.addAttribute("editRecipeFormDTO", editRecipeFormDTO);

        return "recipes/edit";
    }

    @PostMapping("/edit/{recipeId}")
    public String renderEditRecipeForm(Model model, @PathVariable int recipeId, @ModelAttribute @Valid EditRecipeFormDTO editRecipeFormDTO, Errors errors){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()) {
            // recipeId is not found in the database
            return "redirect:/recipes";
        }

        Recipe originalRecipe = optionalRecipe.get();
        boolean userIsAuthor = originalRecipe.getAuthor().equals(model.getAttribute("user"));

        if(! userIsAuthor) {
            // the user trying to edit the recipe is not the author, they shouldn't be allowed to edit it
            return "redirect:/recipes";
        }

        if(errors.hasErrors() ){
            // The proposed updated recipe doesn't meet validation requirements and shouldn't be saved to the database
            model.addAttribute("title", "Edit Recipe");
            model.addAttribute("editRecipeFormDTO", editRecipeFormDTO);
            return "recipes/edit";
        }

        // edit recipe requests meets requirements and is acted on

        originalRecipe.setName(editRecipeFormDTO.getName());
        originalRecipe.setShortDescription(editRecipeFormDTO.getShortDescription());

        originalRecipe.getIngredients().clear();
        originalRecipe.getSteps().clear();
        Recipe savedRecipe = recipeRepository.save(originalRecipe);
        for( String ingredientName : editRecipeFormDTO.getIngredients()) {
            Ingredient newIngredient = new Ingredient(ingredientName);
            newIngredient.setRecipe(savedRecipe);
            ingredientRepository.save(newIngredient);
        }
        for( String stepText : editRecipeFormDTO.getSteps()) {
            RecipeStep newStep = new RecipeStep(stepText);
            newStep.setRecipe(originalRecipe);
            recipeStepRepository.save(newStep);
        }

        return "redirect:/recipes/details/" + originalRecipe.getId();
    }

    @PostMapping("delete")
    public String processDeleteRecipe(@RequestParam Integer recipeId, Model model){
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if(optionalRecipe.isEmpty()){
            // delete request for an id that is not in the database
            return "redirect:/recipes";
        }
        Recipe recipeToDelete = optionalRecipe.get();

        boolean userIsAuthor = recipeToDelete.getAuthor().equals(model.getAttribute("user"));
        if(! userIsAuthor){
            // requesting user doesn't own the recipe and shouldn't be allowed to delete it
            return "redirect:/recipes";
        }

        recipeRepository.delete(recipeToDelete);
        return "redirect:/recipes";
    }

}
