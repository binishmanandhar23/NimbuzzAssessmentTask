package com.nimbuzz.nimbuzzassesmenttask.utils

import android.content.res.Resources
import android.util.Log

fun getFullWidthOfDeviceInPx() = Resources.getSystem().displayMetrics.widthPixels
fun getFullWidthOfDeviceInDp() = Resources.getSystem().displayMetrics.widthPixels / Resources.getSystem().displayMetrics.density
fun getFullHeightOfDeviceInPx() = Resources.getSystem().displayMetrics.heightPixels
fun getFullHeightOfDeviceInDp() = Resources.getSystem().displayMetrics.heightPixels / Resources.getSystem().displayMetrics.density


fun Int.checkIfImageNeedsToBeOfADog(sizeOfList: Int): Boolean{
    var adder = 1
    var startingPoint = 0

    while(startingPoint < sizeOfList /*21*/) { /*Can be replaced by 21 if we are only accounting for (1,3,6,10,15,21)*/
        startingPoint += adder
        if (startingPoint == this)
            return true

        adder++
    }
    return false
}

fun getNumberOfColumns(sizeOfList: Int): Int = (getBaseNumberOfItemsForTriangle(sizeOfList = sizeOfList) + 1) / 2

fun getBaseNumberOfItemsForTriangle(sizeOfList: Int): Int{
    var startingPoint = 1
    var addedValue = startingPoint
    while(addedValue < sizeOfList) {
        startingPoint += 2
        addedValue += startingPoint
    }
    return startingPoint
}

fun getMiddlePosition(sizeOfList: Int): Int = (getBaseNumberOfItemsForTriangle(sizeOfList = sizeOfList) - 1) / 2

fun shouldPopulate(sizeOfList: Int, columnIndex: Int, rowIndex: Int) = rowIndex in (getMiddlePosition(sizeOfList) - columnIndex..getMiddlePosition(sizeOfList) + columnIndex) && (getItemNumberBasedOnColumnAndRowIndex(columnIndex, rowIndex, sizeOfList) < sizeOfList)

fun getItemNumberBasedOnColumnAndRowIndex(columnIndex: Int, rowIndex: Int, sizeOfList: Int): Int{
    var startingPoint = 1
    var addedValue = 0
    for(i in 0..columnIndex){
        addedValue += startingPoint
        if(i == columnIndex){
            ((getMiddlePosition(sizeOfList = sizeOfList) - columnIndex).. (getMiddlePosition(sizeOfList = sizeOfList) + columnIndex)).forEachIndexed { index, j ->
                if(j == rowIndex)
                    return (addedValue - startingPoint) + index
            }
        }
        startingPoint += 2

    }
    return startingPoint
}