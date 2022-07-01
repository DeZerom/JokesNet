package ru.dezerom.jokesnet.screens.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
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
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        label = { Text(text = labelText) },
        visualTransformation = visualTransformation
    )
}

@Composable
fun TextWithPadding(
    text: String
) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
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