package ru.dezerom.jokesnet.screens.widgets

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import ru.dezerom.jokesnet.R

@Composable
        /**
         * Button with provided [onClick] and [text]. Fills max width and has 16dp horizontal and
         * 8dp vertical padding
         */
fun FullWidthButton(onClick: () -> Unit, text: String) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = text)
    }
}

@Composable
        /**
         * TextField that fills max width and and has 16dp horizontal and 8dp vertical padding
         */
fun FullWidthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        label = { Text(text = labelText) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun TextWithPadding(
    text: String,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        fontSize = fontSize
    )
}

@Composable
fun Loading() {
    CircularProgressIndicator(
        modifier = Modifier.fillMaxSize(0.5F)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.loading_joke),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun Error(
    text: String,
    advice: String,
    @DrawableRes drawableRes: Int = R.drawable.ic_baseline_back_hand_24
) {
    Image(
        painter = painterResource(drawableRes),
        contentDescription = "Hand",
        modifier = Modifier
            .fillMaxSize(0.5F)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    )
    TextWithPadding(text = text)
    TextWithPadding(text = advice)
}

@Composable
fun DoNotKnowWTFTheErrorIs() {
    Error(
        text = stringResource(id = R.string.unknown_error_string),
        advice = stringResource(id = R.string.unknow_error_advise),
        drawableRes = R.drawable.ic_baseline_add_task_24
    )
}