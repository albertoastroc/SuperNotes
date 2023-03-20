package com.gmail.pentominto.us.supernotes

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun newInstallScreenAboutNote() {
        composeTestRule
            .onNodeWithText("About this app")
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun newInstallScreenSearchBarText() {
        composeTestRule
            .onNodeWithText("Search Notes...")
            .assertIsDisplayed()
    }

    @Test
    fun newInstallScreenMenuIcon() {
        composeTestRule
            .onNodeWithTag("Search Bar Menu")
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun newInstallScreenXIcon() {
        composeTestRule
            .onNodeWithTag("Search Bar X")
            .assertIsDisplayed()
            .assertHasClickAction()
    }
}
