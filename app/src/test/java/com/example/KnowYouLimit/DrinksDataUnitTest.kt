package com.example.KnowYouLimit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.KnowYouLimit.dto.drinks
import com.example.KnowYouLimit.service.DrinksService
import com.example.KnowYouLimit.ui.main.MainViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DrinksDataUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    lateinit var mvm: MainViewModel

    var drinkService = mockk<DrinksService>()

    @Test
    fun searchForMockkBudLight_returnsBudLight(){
        givenAFeedOfMockkDrinkDataAreAvailable()
        whenSearchForBudLight()
        thenResultContainsBudLight()
    }

    private fun givenAFeedOfMockkDrinkDataAreAvailable() {
        mvm = MainViewModel()
        createMockkData()
    }

    private fun createMockkData() {
        var allDrinksLiveData = MutableLiveData<ArrayList<drinks>>()
        var allDrinks = ArrayList<drinks>()

        //mimmick budlight data as if coming in by JSON
        var budLight = drinks("BudLight", "Beer", "4.9", "12oz")

        //add to array
        allDrinks.add(budLight)

        //post that array to our mimmicked live data
        allDrinksLiveData.postValue(allDrinks)

        every { drinkService.fetchDrinks(any<String>())} returns allDrinksLiveData

        mvm.drinksService = drinkService
    }

    private fun whenSearchForBudLight() {
        mvm.fetchDrinks("BudLight")
    }

    private fun thenResultContainsBudLight() {
        var budLightFound = false

        mvm.drinks.observeForever {
            //check to make sure not null and value is there
            assertNotNull(it)
            assertTrue(it.size > 0)
            it.forEach {
                if(it.name == "BudLight" && it.category == "Beer" && it.abv == "4.9" && it.size == "12oz"){
                    budLightFound = true
                }
            }
        }

        assertTrue(budLightFound)
    }

}
