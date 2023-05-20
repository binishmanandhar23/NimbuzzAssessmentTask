package com.nimbuzz.nimbuzzassesmenttask.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nimbuzz.nimbuzzassesmenttask.data.PickerScreenData

@Composable
fun PickerScreen(
    pickerScreenDataForDog: PickerScreenData,
    pickerScreenDataForCat: PickerScreenData
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
    ) {
        PickerDesign(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            pickerScreenData = pickerScreenDataForDog
        )
        PickerDesign(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            pickerScreenData = pickerScreenDataForCat
        )
    }
}

@Composable
fun PickerDesign(modifier: Modifier = Modifier, pickerScreenData: PickerScreenData) {
    val hapticFeedback = LocalHapticFeedback.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = pickerScreenData.title, style = MaterialTheme.typography.headlineLarge)
        AnimatedContent(pickerScreenData.previewUri) { uri ->
            if (uri != null)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uri)
                        .crossfade(true)
                        .build(),
                    placeholder = rememberVectorPainter(image = Icons.Default.Image),
                    contentDescription = pickerScreenData.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
            else
                Icon(
                    modifier = Modifier.size(120.dp),
                    imageVector = Icons.Default.Image,
                    contentDescription = "Placeholder"
                )
        }
        Button(onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            pickerScreenData.onChoose?.invoke()
        }) {
            Text(text = pickerScreenData.buttonName, style = MaterialTheme.typography.labelMedium)
        }
    }
}