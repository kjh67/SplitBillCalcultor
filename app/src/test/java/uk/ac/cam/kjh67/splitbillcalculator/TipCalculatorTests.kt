package uk.ac.cam.kjh67.splitbillcalculator

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TipCalculatorTests {
    @Test
    fun calculateTip_20PercentNoRoundup() {
        val amount = 10.00
        val tipPercent = 20.00
        val expectedTip = 2.0
        val actualTip = calculateTip(amount, tipPercent, false)
        assertEquals(expectedTip, actualTip, 0.01)
    }
}