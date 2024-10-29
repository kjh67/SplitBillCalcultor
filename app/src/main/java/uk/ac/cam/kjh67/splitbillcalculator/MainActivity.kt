package uk.ac.cam.kjh67.splitbillcalculator

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.cam.kjh67.splitbillcalculator.ui.theme.SplitBillCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplitBillCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplitBillCalculatorLayout()
                }
            }
        }
    }
}

@Composable
fun SplitBillCalculatorLayout() {
    var amountInput by remember { mutableStateOf("") }
    val amount : Double = amountInput.toDoubleOrNull() ?: 0.0

    var tipPercentageInput by remember { mutableStateOf("") }
    val tipPercentage: Double = tipPercentageInput.toDoubleOrNull() ?: 0.0

    var numPeopleInput by remember { mutableStateOf("") }
    val numPeople: Int = numPeopleInput.toIntOrNull() ?: 0

    var roundUp by remember { mutableStateOf(false) }
    val tip = calculateTip(amount, tipPercentage)

    val totalAmount = tip + amount
    val amountPerPerson = calculateAmountPerPerson(totalAmount, numPeople, roundUp)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculateSplitBillText),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.billAmount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = amountInput,
            onValueChange = { amountInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()
        )
        EditNumberField(
            label = R.string.tipPercentage,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = tipPercentageInput,
            onValueChange = { tipPercentageInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()
        )
        EditNumberField(
            label = R.string.numPeopleText,
            leadingIcon = R.drawable.group,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            value = numPeopleInput,
            onValueChange = { numPeopleInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth()
        )
        RoundUpAmountRow(
            roundUp = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Text(
            text = stringResource(
                R.string.tipAmount,
                NumberFormat.getCurrencyInstance().format(tip)
            ),
            style = MaterialTheme.typography.displaySmall,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            text = stringResource(
                R.string.totalAmount,
                NumberFormat.getCurrencyInstance().format(totalAmount)
            ),
            style = MaterialTheme.typography.displaySmall,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            text = stringResource(
                R.string.amountPerPerson,
                NumberFormat.getCurrencyInstance().format(amountPerPerson)
            ),
            style = MaterialTheme.typography.displaySmall,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

@Composable
fun RoundUpAmountRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
    ) {
        Text(text = stringResource(R.string.roundUpSplitAmounts))
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
        )
    }
}

@VisibleForTesting
internal fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean = false): Double {
    var tip = tipPercent/100 * amount
    if (roundUp) {
        tip = kotlin.math.ceil(tip)
    }
    return tip
}

@VisibleForTesting
internal fun calculateAmountPerPerson(totalAmount: Double, numPeople: Int, roundUp: Boolean = false): Double {
    var amountPerPerson = if (numPeople != 0) {totalAmount/numPeople} else {0.0}
    if (roundUp) {
        amountPerPerson = kotlin.math.ceil(amountPerPerson)
    }
    return amountPerPerson
}

@Preview(showBackground = true)
@Composable
fun SplitBillLayoutPreview() {
    SplitBillCalculatorTheme {
        SplitBillCalculatorLayout()
    }
}