package ru.dezerom.jokesnet.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.dezerom.jokesnet.R

sealed class NestedDestinations(val route: String, @StringRes val titleRes: Int, @DrawableRes val iconRes: Int) {
    object PROFILE : NestedDestinations("profile", R.string.profile_string, R.drawable.ic_baseline_person_24)
    object ADD_JOKE: NestedDestinations("add_joke", R.string.add_joke_string, R.drawable.ic_baseline_add_24)
}