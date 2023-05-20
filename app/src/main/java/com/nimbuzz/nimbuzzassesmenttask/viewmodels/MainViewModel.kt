package com.nimbuzz.nimbuzzassesmenttask.viewmodels

import android.net.Uri
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimbuzz.nimbuzzassesmenttask.data.PickerScreenData
import com.nimbuzz.nimbuzzassesmenttask.screens.Screen
import com.nimbuzz.nimbuzzassesmenttask.utils.getBaseNumberOfItemsForTriangle
import com.nimbuzz.nimbuzzassesmenttask.utils.getFullWidthOfDeviceInDp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var _pickerScreenDataForDog = MutableStateFlow(
        PickerScreenData(
            title = "Dog",
            buttonName = "Choose an image of a dog"
        )
    )
    val pickerScreenDataForDog = _pickerScreenDataForDog.asStateFlow()

    private var _pickerScreenDataForCat = MutableStateFlow(
        PickerScreenData(
            title = "Cat",
            buttonName = "Choose an image of a cat",
        )
    )
    val pickerScreenDataForCat = _pickerScreenDataForCat.asStateFlow()

    private var _currentScreen = MutableStateFlow<Screen>(Screen.PickerScreen)
    val currentScreen = _currentScreen.asStateFlow()

    private var _listSize = MutableStateFlow(50)
    val listSize = _listSize.asStateFlow()

    private var _imageSize = MutableStateFlow(50.dp)
    val imageSize = _imageSize.asStateFlow()

    init {
        viewModelScope.launch {
            combine(pickerScreenDataForDog, pickerScreenDataForCat) { dog, cat ->
                if (dog.previewUri != null && cat.previewUri != null)
                    Screen.ListScreen
                else
                    Screen.PickerScreen
            }.collect {
                _currentScreen.update { _ -> it }
            }
        }

        viewModelScope.launch {
            listSize.collect{ size ->
                _imageSize.update { (getFullWidthOfDeviceInDp() / getBaseNumberOfItemsForTriangle(sizeOfList = size).toFloat()).dp }
            }
        }
    }

    fun updatePreviewUriForDog(previewUri: Uri) = _pickerScreenDataForDog.update { it.copy(previewUri = previewUri) }
    fun updatePreviewUriForCat(previewUri: Uri) = _pickerScreenDataForCat.update { it.copy(previewUri = previewUri) }
    fun resetUri() {
        _pickerScreenDataForDog.update { it.copy(previewUri = null) }
        _pickerScreenDataForCat.update { it.copy(previewUri = null) }
    }

    fun changeScreen(screen: Screen) = _currentScreen.update { screen }

    fun updateListSize(size: Int) = _listSize.update { size }
}