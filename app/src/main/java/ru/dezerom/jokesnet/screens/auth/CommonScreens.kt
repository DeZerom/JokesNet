package ru.dezerom.jokesnet.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.dezerom.jokesnet.R

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