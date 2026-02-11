/**
 * 
 */
package edu.ncsu.csc326.coffee_maker.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.ncsu.csc326.coffee_maker.entity.Recipe;

/**
 * Tests Recipe repository
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class RecipeRepositoryTest {
	
	/** Reference to recipe repository */
	@Autowired
	private RecipeRepository recipeRepository;
	
	/** Coffee recipe */
	private Recipe recipe1;
	/** Latte recipe */
	private Recipe recipe2;

	/**
	 * Sets up the test case.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		recipeRepository.deleteAll();
		
		recipe1 = new Recipe("Coffee", 50, 3, 0, 0, 0);
		recipe2 = new Recipe("Latte", 100, 3, 2, 1, 0);

		recipeRepository.save(recipe1);
		recipeRepository.save(recipe2);
	}

	@Test
	public void testGetRecipeByName() {
	    // Test Coffee
	    Optional<Recipe> recipe1 = recipeRepository.findByName("Coffee");
	    Recipe actualRecipe1 = recipe1.get();
	    assertAll("Recipe contents",
	            () -> assertEquals("Coffee", actualRecipe1.getName()),
	            () -> assertEquals(50, actualRecipe1.getPrice()),
	            () -> assertEquals(3, actualRecipe1.getCoffee()),
	            () -> assertEquals(0, actualRecipe1.getMilk()),
	            () -> assertEquals(0, actualRecipe1.getSugar()),
	            () -> assertEquals(0, actualRecipe1.getChocolate()));
	    
	    // Test Latte
	    Optional<Recipe> recipe2 = recipeRepository.findByName("Latte");
        Recipe actualRecipe2 = recipe2.get();
        assertAll("Recipe contents",
                () -> assertEquals("Latte", actualRecipe2.getName()),
                () -> assertEquals(100, actualRecipe2.getPrice()),
                () -> assertEquals(3, actualRecipe2.getCoffee()),
                () -> assertEquals(2, actualRecipe2.getMilk()),
                () -> assertEquals(1, actualRecipe2.getSugar()),
                () -> assertEquals(0, actualRecipe2.getChocolate()));
	    
	}
	
	@Test
	public void testGetRecipeByNameInvalid() {
	    Optional<Recipe> recipe = recipeRepository.findByName("Unknown");
	    assertTrue(recipe.isEmpty());
	}

}
