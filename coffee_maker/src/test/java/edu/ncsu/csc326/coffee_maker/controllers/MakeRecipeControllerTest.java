/**
 *
 */
package edu.ncsu.csc326.coffee_maker.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.coffee_maker.TestUtils;
import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MakeRecipeControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc          mvc;

    /** Reference to recipe repository */
    @Autowired
    private RecipeRepository recipeRepository;

    /** Reference to EntityManager */
    @Autowired
    private EntityManager    entityManager;

    /**
     * Sets up the test case.
     *
     * @throws java.lang.Exception
     *             if error
     */
    @BeforeEach
    public void setUp () throws Exception {
        recipeRepository.deleteAll();
        final Query query = entityManager.createNativeQuery( "TRUNCATE TABLE inventory" );
        query.executeUpdate();
    }

    /**
     * Tests if the make recipes correctly
     *
     * @throws java.lang.Exception
     *             if error
     */
    @Test
    @Transactional
    public void testMakeRecipe () throws Exception {
        // Add one recipe and check if it was added
        final RecipeDto recipeDto = new RecipeDto( "Mocha", 200, 2, 1, 1, 3 );

        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipeDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.name" ).value( "Mocha" ) )
                .andExpect( jsonPath( "$.price" ).value( "200" ) ).andExpect( jsonPath( "$.coffee" ).value( "2" ) )
                .andExpect( jsonPath( "$.milk" ).value( "1" ) ).andExpect( jsonPath( "$.sugar" ).value( "1" ) )
                .andExpect( jsonPath( "$.chocolate" ).value( "3" ) );

        final String recipe = mvc.perform( get( "/api/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( recipe.contains( "Mocha" ) );

        final InventoryDto emptyInventory = new InventoryDto( 1L, 0, 0, 0, 0 );
        final InventoryDto expectedInventory = new InventoryDto( 1L, 10, 20, 30, 40 );

        mvc.perform( get( "/api/inventory" ) ).andExpect( content().string( TestUtils.asJsonString( emptyInventory ) ) )
                .andExpect( status().isOk() );
        mvc.perform( put( "/api/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( expectedInventory ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.coffee" ).value( "10" ) )
                .andExpect( jsonPath( "$.milk" ).value( "20" ) ).andExpect( jsonPath( "$.sugar" ).value( "30" ) )
                .andExpect( jsonPath( "$.chocolate" ).value( "40" ) );
        mvc.perform( get( "/api/inventory" ) )
                .andExpect( content().string( TestUtils.asJsonString( expectedInventory ) ) )
                .andExpect( status().isOk() );
        mvc.perform( post( "/api/makerecipe/Mocha" ).contentType( MediaType.APPLICATION_JSON ).content( "500" )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( "300" ) );
        final InventoryDto updatedInventory = new InventoryDto( 1L, 8, 19, 29, 37 );

        mvc.perform( get( "/api/inventory" ) )
                .andExpect( content().string( TestUtils.asJsonString( updatedInventory ) ) )
                .andExpect( status().isOk() );
    }

    /**
     * Tests recipe when there is not enough money
     *
     * @throws java.lang.Exception
     *             if error
     */
    @Test
    @Transactional
    public void testMakeRecipeNotEnoughMoney () throws Exception {
        // Add one recipe and check if it was added
        final RecipeDto recipeDto = new RecipeDto( "Mocha", 200, 2, 1, 1, 3 );

        mvc.perform( post( "/api/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipeDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.name" ).value( "Mocha" ) )
                .andExpect( jsonPath( "$.price" ).value( "200" ) ).andExpect( jsonPath( "$.coffee" ).value( "2" ) )
                .andExpect( jsonPath( "$.milk" ).value( "1" ) ).andExpect( jsonPath( "$.sugar" ).value( "1" ) )
                .andExpect( jsonPath( "$.chocolate" ).value( "3" ) );

        final String recipe = mvc.perform( get( "/api/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( recipe.contains( "Mocha" ) );

        final InventoryDto emptyInventory = new InventoryDto( 1L, 0, 0, 0, 0 );
        final InventoryDto expectedInventory = new InventoryDto( 1L, 10, 20, 30, 40 );

        mvc.perform( get( "/api/inventory" ) ).andExpect( content().string( TestUtils.asJsonString( emptyInventory ) ) )
                .andExpect( status().isOk() );
        mvc.perform( put( "/api/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( expectedInventory ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.coffee" ).value( "10" ) )
                .andExpect( jsonPath( "$.milk" ).value( "20" ) ).andExpect( jsonPath( "$.sugar" ).value( "30" ) )
                .andExpect( jsonPath( "$.chocolate" ).value( "40" ) );
        mvc.perform( get( "/api/inventory" ) )
                .andExpect( content().string( TestUtils.asJsonString( expectedInventory ) ) )
                .andExpect( status().isOk() );
        mvc.perform( post( "/api/makerecipe/Mocha" ).contentType( MediaType.APPLICATION_JSON ).content( "100" )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isConflict() )
                .andExpect( content().string( "100" ) );
        final InventoryDto updatedInventory = new InventoryDto( 1L, 10, 20, 30, 40 );

        mvc.perform( get( "/api/inventory" ) )
                .andExpect( content().string( TestUtils.asJsonString( updatedInventory ) ) )
                .andExpect( status().isOk() );
    }

}
