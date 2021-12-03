package org.launchcode.hungr.data;

import org.launchcode.hungr.models.Recipe;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {

    public List<Recipe> findByNameContainingIgnoreCase(String qString);
}
