package com.nimbuzz.nimbuzzassesmenttask.screens

sealed class Screen(val route: String){
    object PickerScreen: Screen(route = "picker_screen")
    object ListScreen: Screen(route = "list_screen")
}
