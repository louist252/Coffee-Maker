package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {
	
	/** Mock MVC for testing controller */
	@Autowired
	private MockMvc mvc;

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
	 * Testing the RecipeController.getRecipes() method.
	 * @throws Exception if there is an error in retrieving recipes from the DB.
	 */
	@Test
	@Transactional
	public void testGetRecipes() throws Exception {
		String recipe = mvc.perform(get("/api/recipes"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		assertFalse(recipe.contains("Mocha"));
	}
	
	/**
	 * Testing the RecipeController.createRecipe() method.
	 * @throws Exception if there are any errors executing the test.
	 */
	@Test
	@Transactional
	public void testCreateRecipe() throws Exception {
		RecipeDto recipeDto = new RecipeDto("Mocha", 200, 2, 1, 1, 3);
		
		mvc.perform(post("/api/recipes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(recipeDto))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Mocha"))
				.andExpect(jsonPath("$.price").value("200"))
				.andExpect(jsonPath("$.coffee").value("2"))
				.andExpect(jsonPath("$.milk").value("1"))
				.andExpect(jsonPath("$.sugar").value("1"))
				.andExpect(jsonPath("$.chocolate").value("3"));
		
		String recipe = mvc.perform(get("/api/recipes"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		assertTrue(recipe.contains("Mocha"));
	}
	
	/**
	 * Testing the RecipeController.getRecipe(String) method.
	 * @throws Exception if there are any errors executing the test.
	 */
	@Test
	@Transactional
	public void testGetRecipe() throws Exception {
		RecipeDto recipeDto = new RecipeDto("Mocha", 200, 2, 1, 1, 3);
		
		mvc.perform(post("/api/recipes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(recipeDto))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Mocha"))
				.andExpect(jsonPath("$.price").value("200"))
				.andExpect(jsonPath("$.coffee").value("2"))
				.andExpect(jsonPath("$.milk").value("1"))
				.andExpect(jsonPath("$.sugar").value("1"))
				.andExpect(jsonPath("$.chocolate").value("3"));
		
	    mvc.perform(get("/api/recipes/{name}", "Mocha")
	            .accept(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.name").value("Mocha"))
	        .andExpect(jsonPath("$.price").value(200))
	        .andExpect(jsonPath("$.coffee").value(2))
	        .andExpect(jsonPath("$.milk").value(1))
	        .andExpect(jsonPath("$.sugar").value(1))
	        .andExpect(jsonPath("$.chocolate").value(3));
	}
	
	/**
	 * Testing the RecipeController.deleteRecipe(Long) method.
	 * @throws Exception if there are any errors executing the test.
	 */
	@Test
	@Transactional
	public void testDeleteRecipe() throws Exception {
		RecipeDto recipeDto = new RecipeDto("Mocha", 200, 2, 1, 1, 3);

	    String response = mvc.perform(post("/api/recipes")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(TestUtils.asJsonString(recipeDto))
	            .accept(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andReturn()
	        .getResponse()
	        .getContentAsString();

	    Integer idInt = JsonPath.read(response, "$.id");
	    Long id = idInt.longValue();

	    mvc.perform(delete("/api/recipes/{id}", id))
	        .andExpect(status().isOk())
	        .andExpect(content().string("Recipe deleted successfully."));
	}
	
}
