package ru.dezerom.jokesnet.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ru.dezerom.jokesnet.R

sealed class NestedDestinations(val route: String, @StringRes val titleRes: Int, @DrawableRes val iconRes: Int) {
    object PROFILE : NestedDestinations("profile", R.string.profile_string, R.drawable.ic_baseline_person_24)
    object SOME_SCREEN : NestedDestinations("some_screen", R.string.app_name, R.drawable.ic_baseline_check_24)
}