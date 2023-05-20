package com.nimbuzz.nimbuzzassesmenttask.screens

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nimbuzz.nimbuzzassesmenttask.utils.checkIfImageNeedsToBeOfADog
import com.nimbuzz.nimbuzzassesmenttask.utils.getBaseNumberOfItemsForTriangle
import com.nimbuzz.nimbuzzassesmenttask.utils.getItemNumberBasedOnColumnAndRowIndex
import com.nimbuzz.nimbuzzassesmenttask.utils.getNumberOfColumns
import com.nimbuzz.nimbuzzassesmenttask.utils.shouldPopulate
import kotlinx.coroutines.flow.StateFlow


@Composable
fun ListScreen(
    uriForDog: Uri,
    uriForCat: Uri,
    imageSize: Dp,
    sizeOfList: Int = 50,
    onSizeChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Card(
                modifier = Modifier.align(Alignment.Center),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(text = "List Size: ", style = MaterialTheme.typography.headlineSmall)
                        AnimatedContent(targetState = sizeOfList,
                            transitionSpec = {
                                // Compare the incoming number with the previous number.
                                if (targetState > initialState) {
                                    // If the target number is larger, it slides up and fades in
                                    // while the initial (smaller) number slides up and fades out.
                                    slideInVertically { height -> height } + fadeIn() togetherWith
                                            slideOutVertically { height -> -height } + fadeOut()
                                } else {
                                    // If the target number is smaller, it slides down and fades in
                                    // while the initial number slides down and fades out.
                                    slideInVertically { height -> -height } + fadeIn() togetherWith
                                            slideOutVertically { height -> height } + fadeOut()
                                }.using(
                                    // Disable clipping since the faded slide-in/out should
                                    // be displayed out of bounds.
                                    SizeTransform(clip = false)
                                )
                            }) {
                            Text(text = "$it", style = MaterialTheme.typography.headlineSmall)
                        }
                    }
                    Spacer(modifier = Modifier.size(10.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(onClick = { onSizeChange(sizeOfList + 1) }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.Default.ArrowCircleUp,
                                contentDescription = "Increase lise size"
                            )
                        }
                        IconButton(onClick = { onSizeChange(sizeOfList - 1) }) {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = Icons.Default.ArrowCircleDown,
                                contentDescription = "Decrease lise size"
                            )
                        }
                    }
                }
            }
        }
        ListDesign(
            uriForDog = uriForDog,
            uriForCat = uriForCat,
            imageSize = imageSize,
            sizeOfList = sizeOfList
        )
    }
}

@Composable
private fun ListDesign(uriForDog: Uri, uriForCat: Uri, imageSize: Dp, sizeOfList: Int) {
    val numberOfColumns = remember(sizeOfList) {
        getNumberOfColumns(sizeOfList = sizeOfList)
    }
    val totalItemsInARow = remember(sizeOfList) {
        getBaseNumberOfItemsForTriangle(sizeOfList)
    }
    LazyColumn {
        items(count = numberOfColumns) { columnIndex ->
            ListRow(
                uriForDog = uriForDog,
                uriForCat = uriForCat,
                imageSize = imageSize,
                columnIndex = columnIndex,
                totalItemsInARow = totalItemsInARow,
                sizeOfList = sizeOfList
            )
        }
    }
}

@Composable
private fun ListRow(
    uriForDog: Uri,
    uriForCat: Uri,
    imageSize: Dp,
    columnIndex: Int,
    totalItemsInARow: Int,
    sizeOfList: Int,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        for(rowIndex in 0 until totalItemsInARow){
            val actualItemIndex by remember(sizeOfList, columnIndex, rowIndex) {
                derivedStateOf {
                    getItemNumberBasedOnColumnAndRowIndex(columnIndex, rowIndex, sizeOfList)
                }
            }
            val populate by remember(sizeOfList, columnIndex, rowIndex) {
                derivedStateOf {
                    shouldPopulate(sizeOfList, columnIndex, rowIndex)
                }
            }

            if (!populate)
                Spacer(modifier = Modifier.size(imageSize))


            AnimatedVisibility(visible = populate) {
                val isDog = remember(actualItemIndex) {
                    actualItemIndex.checkIfImageNeedsToBeOfADog(sizeOfList = sizeOfList)
                }
                ImageDesign(
                    imageSize = imageSize,
                    uri = if (isDog) uriForDog else uriForCat,
                    contentDescription = if (isDog) "Dog" else "Cat"
                )
            }
        }
    }
}

@Composable
private fun ImageDesign(imageSize: Dp, uri: Uri, contentDescription: String) = AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(uri)
        .crossfade(true)
        .build(),
    placeholder = rememberVectorPainter(image = Icons.Default.Image),
    contentDescription = contentDescription,
    contentScale = ContentScale.Crop,
    modifier = Modifier
        .size(imageSize)
        .clip(RoundedCornerShape(5.dp))
)
