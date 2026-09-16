package com.example.midtermexam

import org.junit.Test
import org.junit.Assert.*

class ExampleUnitTest {
    @Test
    fun testDishViewModelCrud() {
        val vm = DishViewModel()
        
        // Initial state
        assertEquals(2, vm.dishes.value.size)
        assertEquals("Chicken Adobo", vm.getDish(1)?.name)
        assertEquals("Sinigang na Baboy", vm.getDish(2)?.name)

        // CREATE Dish
        vm.addDish("Kare-Kare")
        assertEquals(3, vm.dishes.value.size)
        val kareKare = vm.dishes.value.last()
        assertEquals("Kare-Kare", kareKare.name)

        // Blank name ignored
        vm.addDish("   ")
        assertEquals(3, vm.dishes.value.size)

        // UPDATE Dish (TODO 1)
        vm.updateDish(kareKare.id, "Beef Kare-Kare")
        assertEquals("Beef Kare-Kare", vm.getDish(kareKare.id)?.name)
        vm.updateDish(kareKare.id, "   ")
        assertEquals("Beef Kare-Kare", vm.getDish(kareKare.id)?.name)

        // CREATE Recipe (TODO 3)
        vm.addRecipe(kareKare.id, "Boil the beef")
        vm.addRecipe(kareKare.id, "Add peanut sauce")
        vm.addRecipe(kareKare.id, "   ") // blank ignored
        val updatedKareKare = vm.getDish(kareKare.id)!!
        assertEquals(2, updatedKareKare.recipes.size)
        assertEquals("Boil the beef", updatedKareKare.recipes[0].text)
        assertEquals("Add peanut sauce", updatedKareKare.recipes[1].text)

        // UPDATE Recipe (TODO 4)
        val step1Id = updatedKareKare.recipes[0].id
        vm.updateRecipe(kareKare.id, step1Id, "Boil the oxtail and beef")
        vm.updateRecipe(kareKare.id, step1Id, "   ") // blank ignored
        assertEquals("Boil the oxtail and beef", vm.getDish(kareKare.id)!!.recipes[0].text)

        // DELETE Recipe (TODO 5)
        vm.deleteRecipe(kareKare.id, step1Id)
        assertEquals(1, vm.getDish(kareKare.id)!!.recipes.size)
        assertEquals("Add peanut sauce", vm.getDish(kareKare.id)!!.recipes[0].text)

        // DELETE Dish (TODO 2)
        vm.deleteDish(kareKare.id)
        assertEquals(2, vm.dishes.value.size)
        assertNull(vm.getDish(kareKare.id))
    }
}
