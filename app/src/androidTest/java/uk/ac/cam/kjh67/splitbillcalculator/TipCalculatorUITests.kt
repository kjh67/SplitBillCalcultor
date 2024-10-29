package uk.ac.cam.kjh67.splitbillcalculator

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import uk.ac.cam.kjh67.splitbillcalculator.ui.theme.SplitBillCalculatorTheme
import java.text.NumberFormat

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)
class TipCalculatorUITests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip() {
        composeTestRule.setContent {
            SplitBillCalculatorTheme {
                SplitBillCalculatorLayout()
            }
        }
        composeTestRule.onNodeWithText("Bill amount")
            .performTextClearance()
        composeTestRule.onNodeWithText("Bill amount")
            .performTextInput("10")
        composeTestRule.onNodeWithText("Tip percentage")
            .performTextClearance()
        composeTestRule.onNodeWithText("Tip percentage")
            .performTextInput("20")
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        composeTestRule.onNodeWithText("Tip amount: $expectedTip").assertExists(
            "No node with this text was found."
        )
    }
}