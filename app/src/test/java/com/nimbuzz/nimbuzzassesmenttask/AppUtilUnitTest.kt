package com.nimbuzz.nimbuzzassesmenttask

import com.nimbuzz.nimbuzzassesmenttask.utils.checkIfImageNeedsToBeOfADog
import com.nimbuzz.nimbuzzassesmenttask.utils.getBaseNumberOfItemsForTriangle
import com.nimbuzz.nimbuzzassesmenttask.utils.shouldPopulate
import org.junit.Assert
import org.junit.Test

class AppUtilUnitTest {
    @Test
    fun mainLogicTest(){
        val sizeOfList = 50
        val index = 21

        val indexesForDog = listOf(1,3,6,10,15,21)

        val condition = index.checkIfImageNeedsToBeOfADog(sizeOfList = sizeOfList)

        Assert.assertEquals(indexesForDog.contains(index), condition)
    }

    @Test
    fun baseValueLogicTest(){
        val sizeOfList = 50
        val baseValue = getBaseNumberOfItemsForTriangle(sizeOfList)

        Assert.assertEquals(15, baseValue)
    }

    @Test
    fun shouldPopulateLogicTest(){
        val sizeOfList = 50
        val value = shouldPopulate(sizeOfList = sizeOfList, columnIndex = 7, rowIndex = 1)
        Assert.assertEquals(false, value)
    }

    @Test
    fun getItemNumberBasedOnColumnAndRowIndex(){
        val sizeOfList = 50
        val columnIndex = 2
        val rowIndex = 8

        val itemPosition = com.nimbuzz.nimbuzzassesmenttask.utils.getItemNumberBasedOnColumnAndRowIndex(columnIndex, rowIndex, sizeOfList)

        Assert.assertEquals(7, itemPosition)
    }
}