/**
 *
 */
package edu.ncsu.csc326.coffee_maker.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import edu.ncsu.csc326.coffee_maker.dto.InventoryDto;
import edu.ncsu.csc326.coffee_maker.dto.RecipeDto;
import edu.ncsu.csc326.coffee_maker.repositories.RecipeRepository;

/**
 *
 */
@SpringBootTest
class MakeRecipeServiceTest {

    @Autowired
    private RecipeService     recipeService;

    @Autowired
    private RecipeRepository  recipeRepository;

    @Autowired
    private InventoryService  inventoryService;

    /** Reference to MakeRecipeService */
    @Autowired
    private MakeRecipeService makeRecipeService;

    /**
     * Sets up the test case.
     *
     * @throws java.lang.Exception
     *             if error
     */
    @BeforeEach
    public void setUp () throws Exception {
        recipeRepository.deleteAll();

    }

    /**
     * Test makeRecipe with all ingredients
     */
    @Test
    void testMakeRecipe () {
        final RecipeDto recipeDto = new RecipeDto( "Coffee", 50, 2, 1, 1, 0 );
        final RecipeDto savedRecipe = recipeService.createRecipe( recipeDto );
        final InventoryDto inventoryDto = new InventoryDto( 5, 9, 14, 23 );
        final InventoryDto createdInventoryDto = inventoryService.createInventory( inventoryDto );

        assertTrue( makeRecipeService.makeRecipe( createdInventoryDto, savedRecipe ) );

    }

    @Test
    void testMakeRecipeNotaEnoughCoffe () {
        final RecipeDto recipeDto = new RecipeDto( "Coffee", 50, 2, 1, 1, 0 );
        final RecipeDto savedRecipe = recipeService.createRecipe( recipeDto );
        final InventoryDto inventoryDto = new InventoryDto( 0, 9, 14, 23 );
        final InventoryDto createdInventoryDto = inventoryService.createInventory( inventoryDto );

        assertFalse( makeRecipeService.makeRecipe( createdInventoryDto, savedRecipe ) );

    }

}
