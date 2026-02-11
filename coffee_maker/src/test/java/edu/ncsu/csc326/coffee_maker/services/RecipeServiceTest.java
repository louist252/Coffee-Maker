/**
 * 
 */
package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.exception.ResourceNotFoundException;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;

/**
 * 
 * 
 * @author Sophia Nunez & Rujul Waval
 */
@SpringBootTest
public class RecipeServiceTest {
	
	@Autowired
	private RecipeService recipeService;

	/** Reference to recipe repository */
	@Autowired
	private RecipeRepository recipeRepository;

	/**
	 * Sets up the test case.
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		recipeRepository.deleteAll();
	}

	/**
	 * Test method for createRecipe() {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#createRecipe(edu.ncsu.csc326.coffee_maker.dto.RecipeDto)}.
	 */
	@Test
	@Transactional
	void testCreateRecipe() {
		RecipeDto recipeDto = new RecipeDto("Coffee", 50, 2, 1, 1, 0);	
		RecipeDto savedRecipe = recipeService.createRecipe(recipeDto);
		assertAll("Recipe contents",
				() -> assertTrue(savedRecipe.getId() > 1L),
				() -> assertEquals("Coffee", savedRecipe.getName()),
				() -> assertEquals(50, savedRecipe.getPrice()),
				() -> assertEquals(2, savedRecipe.getCoffee()),
				() -> assertEquals(1, savedRecipe.getMilk()),
				() -> assertEquals(1, savedRecipe.getSugar()),
				() -> assertEquals(0, savedRecipe.getChocolate()));
		
		RecipeDto retrievedRecipe = recipeService.getRecipeById(savedRecipe.getId());
		assertAll("Recipe contents",
				() -> assertEquals(savedRecipe.getId(), retrievedRecipe.getId()),
				() -> assertEquals("Coffee", retrievedRecipe.getName()),
				() -> assertEquals(50, retrievedRecipe.getPrice()),
				() -> assertEquals(2, retrievedRecipe.getCoffee()),
				() -> assertEquals(1, retrievedRecipe.getMilk()),
				() -> assertEquals(1, retrievedRecipe.getSugar()),
				() -> assertEquals(0, retrievedRecipe.getChocolate()));
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#isDuplicateName(java.lang.String)}.
	 */
	@Test
	@Transactional
	void testIsDuplicateName() {
		RecipeDto recipeDto = new RecipeDto("Coffee", 50, 2, 1, 1, 0);	
		recipeService.createRecipe(recipeDto);
		
		assertFalse(recipeService.isDuplicateName("Tea"));
		assertTrue(recipeService.isDuplicateName("Coffee"));
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#getAllRecipes()}.
	 */
	@Test
	@Transactional
	void testGetAllRecipes() {
		// empty list
		List<RecipeDto> recipeList = recipeService.getAllRecipes();
		assertEquals(0, recipeList.size());

		// add recipe
		RecipeDto recipeDto = new RecipeDto("Coffee", 50, 2, 1, 1, 0);	
		RecipeDto savedRecipe = recipeService.createRecipe(recipeDto);
		
		// should have Coffee recipe
		recipeList = recipeService.getAllRecipes();
		assertEquals(1, recipeList.size());
		RecipeDto firstRecipe = recipeList.get(0);
		assertAll("Recipe contents",
				() -> assertTrue(savedRecipe.getId() > 1L),
				() -> assertEquals(savedRecipe.getId(), firstRecipe.getId()),
				() -> assertEquals(savedRecipe.getName(), firstRecipe.getName()),
				() -> assertEquals(savedRecipe.getPrice(), firstRecipe.getPrice()),
				() -> assertEquals(savedRecipe.getCoffee(), firstRecipe.getCoffee()),
				() -> assertEquals(savedRecipe.getMilk(), firstRecipe.getMilk()),
				() -> assertEquals(savedRecipe.getSugar(), firstRecipe.getSugar()),
				() -> assertEquals(savedRecipe.getChocolate(), firstRecipe.getChocolate()));
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#updateRecipe(java.lang.Long, edu.ncsu.csc326.coffee_maker.dto.RecipeDto)}.
	 */
	@Test
	void testUpdateRecipe() {
		RecipeDto recipeDto = new RecipeDto("Coffee", 50, 2, 1, 1, 0);	
		RecipeDto savedRecipe = recipeService.createRecipe(recipeDto);
		assertAll("Recipe contents",
				() -> assertTrue(savedRecipe.getId() > 1L),
				() -> assertEquals("Coffee", savedRecipe.getName()),
				() -> assertEquals(50, savedRecipe.getPrice()),
				() -> assertEquals(2, savedRecipe.getCoffee()),
				() -> assertEquals(1, savedRecipe.getMilk()),
				() -> assertEquals(1, savedRecipe.getSugar()),
				() -> assertEquals(0, savedRecipe.getChocolate()));
		
		// update name, price, and first ingredient
		RecipeDto recipeDtoUpdate = new RecipeDto("Mocha", 40, 4, 1, 1, 0);	
		RecipeDto updatedRecipe = recipeService.updateRecipe(savedRecipe.getId(), recipeDtoUpdate);
		
		assertAll("Recipe contents",
				() -> assertTrue(savedRecipe.getId() > 1L),
				() -> assertEquals("Mocha", updatedRecipe.getName()),
				() -> assertEquals(40, updatedRecipe.getPrice()),
				() -> assertEquals(4, updatedRecipe.getCoffee()),
				() -> assertEquals(1, updatedRecipe.getMilk()),
				() -> assertEquals(1, updatedRecipe.getSugar()),
				() -> assertEquals(0, updatedRecipe.getChocolate()));
		
		// update just one field
		recipeDtoUpdate.setSugar(3);
		RecipeDto updated2 = recipeService.updateRecipe(savedRecipe.getId(), recipeDtoUpdate);
		assertAll("Recipe contents",
				() -> assertTrue(savedRecipe.getId() > 1L),
				() -> assertEquals("Mocha", updated2.getName()),
				() -> assertEquals(40, updated2.getPrice()),
				() -> assertEquals(4, updated2.getCoffee()),
				() -> assertEquals(1, updated2.getMilk()),
				() -> assertEquals(3, updated2.getSugar()),
				() -> assertEquals(0, updated2.getChocolate()));
		
		// update all ingredient fields
		recipeDtoUpdate.setCoffee(5);
		recipeDtoUpdate.setMilk(6);
		recipeDtoUpdate.setSugar(7);
		recipeDtoUpdate.setChocolate(8);
		RecipeDto updated3 = recipeService.updateRecipe(savedRecipe.getId(), recipeDtoUpdate);
		
		assertAll("Recipe contents",
				() -> assertTrue(savedRecipe.getId() > 1L),
				() -> assertEquals("Mocha", updated3.getName()),
				() -> assertEquals(40, updated3.getPrice()),
				() -> assertEquals(5, updated3.getCoffee()),
				() -> assertEquals(6, updated3.getMilk()),
				() -> assertEquals(7, updated3.getSugar()),
				() -> assertEquals(8, updated3.getChocolate()));
		
		
		// try to update non-existent recipe ID (throws)
		assertThrows(ResourceNotFoundException.class, () -> recipeService.updateRecipe((long)-4, recipeDtoUpdate));
	}

	/**
	 * Test method for {@link edu.ncsu.csc326.coffee_maker.services.RecipeService#deleteRecipe(java.lang.Long)}.
	 */
	@Test
	void testDeleteRecipe() {

		RecipeDto recipeDto = new RecipeDto("Coffee", 50, 2, 1, 1, 0);	
		RecipeDto savedRecipe = recipeService.createRecipe(recipeDto);
		assertAll("Recipe contents",
				() -> assertTrue(savedRecipe.getId() > 1L),
				() -> assertEquals("Coffee", savedRecipe.getName()),
				() -> assertEquals(50, savedRecipe.getPrice()),
				() -> assertEquals(2, savedRecipe.getCoffee()),
				() -> assertEquals(1, savedRecipe.getMilk()),
				() -> assertEquals(1, savedRecipe.getSugar()),
				() -> assertEquals(0, savedRecipe.getChocolate()));
		
		recipeService.deleteRecipe(savedRecipe.getId());
		
		assertThrows(ResourceNotFoundException.class, () -> recipeService.getRecipeById(savedRecipe.getId()));
		
		assertThrows(ResourceNotFoundException.class, () -> recipeService.deleteRecipe((long)-4));
	
	}
}
