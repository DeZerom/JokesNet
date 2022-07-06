package ru.dezerom.jokesnet.db.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProfileInfo")
data class DbProfileInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val login: String,
    val jokesAdded: Int,
    val isValid: Boolean
)
