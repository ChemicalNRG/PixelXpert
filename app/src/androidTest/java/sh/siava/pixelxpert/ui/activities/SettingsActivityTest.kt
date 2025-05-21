package sh.siava.pixelxpert.ui.activities

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test
import sh.siava.pixelxpert.ui.theme.PixelXpertTheme
import java.util.Locale

class SettingsActivityTest {

    // Rule to launch SettingsActivity itself
    @get:Rule
    val androidComposeRule = createAndroidComposeRule<SettingsActivity>()

    // Rule for testing Composables in isolation
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun activityNavigationItemsAreDisplayedTest() {
        // This test uses androidComposeRule to launch the actual SettingsActivity
        // It implicitly tests that the SettingsActivityLayout is set as content
        // and that the default (phone or tablet based on test environment) navigation is shown.
        androidComposeRule.onNodeWithText("Home").assertIsDisplayed()
        androidComposeRule.onNodeWithText("Updates").assertIsDisplayed()
        androidComposeRule.onNodeWithText("Hooks").assertIsDisplayed()
        androidComposeRule.onNodeWithText("Settings").assertIsDisplayed()
    }

    @Test
    fun phoneLayoutDisplaysNavigationBarWithItemsTest() {
        // Test the SettingsActivityLayout composable directly for phone layout
        composeTestRule.setContent {
            PixelXpertTheme {
                SettingsActivityLayout(isTablet = false, navigateTo = {})
            }
        }
        composeTestRule.onRoot().printToLog("PhoneLayout") // For debugging
        // Check for NavigationBar items (which are present on phone layout)
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Updates").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hooks").assertIsDisplayed()
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()

        // Specifically verify "Home" is part of a NavigationBar setup.
        // This is a bit tricky without specific test tags on NavigationBar vs NavigationRail.
        // However, the structure of SettingsActivityLayout uses distinct parent Composables (NavigationBar vs NavigationRail).
        // For this test, asserting the items are displayed when isTablet=false implies NavigationBar is used.
    }

    @Test
    fun tabletLayoutDisplaysNavigationRailWithItemsTest() {
        // Test the SettingsActivityLayout composable directly for tablet layout
        composeTestRule.setContent {
            PixelXpertTheme {
                SettingsActivityLayout(isTablet = true, navigateTo = {})
            }
        }
        composeTestRule.onRoot().printToLog("TabletLayout") // For debugging
        // Check for NavigationRail items (which are present on tablet layout)
        composeTestRule.onNodeWithText("Home").assertIsDisplayed()
        composeTestRule.onNodeWithText("Updates").assertIsDisplayed()
        composeTestRule.onNodeWithText("Hooks").assertIsDisplayed()
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()

        // Similar to the phone test, asserting items are displayed when isTablet=true implies NavigationRail.
    }

    @Test
    fun navigationItemClickPassesCorrectRouteTest() {
        val clickedRoutes = mutableListOf<String>()
        val navigateToLambda: (String) -> Unit = { route ->
            clickedRoutes.add(route)
        }

        composeTestRule.setContent {
            PixelXpertTheme {
                // Testing with phone layout, tablet would be similar for item clicks
                SettingsActivityLayout(isTablet = false, navigateTo = navigateToLambda)
            }
        }

        val itemToClick = "Updates"
        composeTestRule.onNodeWithText(itemToClick).performClick()
        assert(clickedRoutes.contains(itemToClick.lowercase(Locale.getDefault()))) {
            "Expected route '${itemToClick.lowercase(Locale.getDefault())}' not found in clicked routes: $clickedRoutes"
        }

        val settingsItemToClick = "Settings"
        composeTestRule.onNodeWithText(settingsItemToClick).performClick()
        assert(clickedRoutes.contains(settingsItemToClick.lowercase(Locale.getDefault()))) {
            "Expected route '${settingsItemToClick.lowercase(Locale.getDefault())}' not found in clicked routes: $clickedRoutes"
        }
    }
}
