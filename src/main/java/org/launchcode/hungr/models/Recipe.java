package org.launchcode.hungr.models;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe extends AbstractEntity{

    @NotBlank(message="Name must not be blank.")
    @Size(max=100, message="Name must be less than 100 characters")
    private String name;

    @NotBlank(message="Description must not be blank")
    @Size(max=240, message="Description must be less than 240 characters")
    private String shortDescription;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "recipe_id")
    private final List<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "recipe_id")
    private final List<RecipeStep> steps = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @ManyToMany(mappedBy = "favoriteRecipes")
    private final List<User> favoritedByUsers = new ArrayList<>();

    public Recipe() {}

    public Recipe(String name, String shortDescription) {
        this.name = name;
        this.shortDescription = shortDescription;
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

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<User> getFavoritedByUsers() {
        return favoritedByUsers;
    }

    public int favoriteCount() {
        return favoritedByUsers.size();
    }

    public boolean isUserFavorite(User user) {
        return favoritedByUsers.contains(user);
    }
}
