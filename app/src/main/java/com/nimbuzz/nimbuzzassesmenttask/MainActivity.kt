package com.nimbuzz.nimbuzzassesmenttask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.nimbuzz.nimbuzzassesmenttask.navigators.customAnimatedComposable
import com.nimbuzz.nimbuzzassesmenttask.navigators.navigator
import com.nimbuzz.nimbuzzassesmenttask.navigators.rememberCustomAnimatedNavController
import com.nimbuzz.nimbuzzassesmenttask.screens.ListScreen
import com.nimbuzz.nimbuzzassesmenttask.screens.PickerScreen
import com.nimbuzz.nimbuzzassesmenttask.screens.Screen
import com.nimbuzz.nimbuzzassesmenttask.ui.theme.NimbuzzAssesmentTaskTheme
import com.nimbuzz.nimbuzzassesmenttask.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    private val pickMediaForDog =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null)
                mainViewModel.updatePreviewUriForDog(previewUri = uri)
        }
    private val pickMediaForCat =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null)
                mainViewModel.updatePreviewUriForCat(previewUri = uri)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mainNavController = rememberCustomAnimatedNavController()
            val currentScreen by mainViewModel.currentScreen.collectAsState()
            LaunchedEffect(key1 = currentScreen) {
                mainNavController.navigator(route = currentScreen.route)
            }

            NimbuzzAssesmentTaskTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavHost(
                        mainNavController = mainNavController,
                        currentScreen = currentScreen
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun MainNavHost(
        mainNavController: NavHostController,
        currentScreen: Screen,
        startDestination: Screen = Screen.PickerScreen
    ) {
        val pickerScreenDataForDog by mainViewModel.pickerScreenDataForDog.collectAsState()
        val pickerScreenDataForCat by mainViewModel.pickerScreenDataForCat.collectAsState()

        AnimatedNavHost(
            navController = mainNavController,
            startDestination = startDestination.route
        ) {
            customAnimatedComposable(route = Screen.PickerScreen.route) {
                PickerScreen(pickerScreenDataForDog = pickerScreenDataForDog.copy(onChoose = {
                    pickMediaForDog.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }), pickerScreenDataForCat = pickerScreenDataForCat.copy(onChoose = {
                    pickMediaForCat.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }))
            }
            customAnimatedComposable(route = Screen.ListScreen.route) {
                val imageSize by mainViewModel.imageSize.collectAsState()
                val sizeOfList by mainViewModel.listSize.collectAsState()
                val showList by remember(pickerScreenDataForDog, pickerScreenDataForCat) {
                    derivedStateOf {
                        pickerScreenDataForDog.previewUri != null && pickerScreenDataForCat.previewUri != null
                    }
                }
                BackHandler {
                    mainViewModel.resetUri()
                }
                if (showList)
                    ListScreen(
                        uriForDog = pickerScreenDataForDog.previewUri!!,
                        uriForCat = pickerScreenDataForCat.previewUri!!,
                        imageSize = imageSize,
                        sizeOfList = sizeOfList,
                        onSizeChange = mainViewModel::updateListSize
                    )
            }
        }
    }
}