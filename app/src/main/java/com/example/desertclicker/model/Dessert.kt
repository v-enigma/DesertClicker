package com.example.desertclicker.model

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.example.desertclicker.R
import com.example.desertclicker.data.DataSource
import com.example.desertclicker.data.DataSource.desserts
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.Serializable

data class Dessert(val imageId:Int, val price:Int, var startProductionAmount:Int ,)

data class DesertUI(
    val currentDessertIndex: Int =0,
    val dessert: Dessert = desserts[currentDessertIndex],
    val revenue: Int = 0,
    val dessertSold: Int = 0,

)


class DessertViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DesertUI())
    val uiState: StateFlow<DesertUI>
        get() = _uiState.asStateFlow()


    fun updateUiState(updatedRevenue: Int, dessertSold: Int) {
        val currentIndex =  determineDessertIndex(dessertSold)
        _uiState.update {

            currentState ->  currentState.copy(
                currentDessertIndex = currentIndex,
                dessert = desserts[currentIndex],
                revenue = updatedRevenue,
                dessertSold = dessertSold
            )

        }
    }
    private fun determineDessertIndex(dessertsSold: Int): Int {
        var dessertIndex = 0
        for (index in desserts.indices) {
            if (dessertsSold >= desserts[index].startProductionAmount) {
                dessertIndex = index
            }


            else {
                // The list of desserts is sorted by startProductionAmount. As you sell more
                // desserts, you'll start producing more expensive desserts as determined by
                // startProductionAmount. We know to break as soon as we see a dessert who's
                // "startProductionAmount" is greater than the amount sold.
                break
            }
        }
        return dessertIndex
    }

}