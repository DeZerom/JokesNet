package ru.dezerom.jokesnet.db.token

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Token(
    @PrimaryKey val token: String
)