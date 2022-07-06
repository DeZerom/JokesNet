package ru.dezerom.jokesnet.db.converters

import androidx.room.TypeConverter

class BooleanIntConverter {

    @TypeConverter
    fun toBoolean(value: Int): Boolean {
        return value == 1
    }

    @TypeConverter
    fun toInt(value: Boolean): Int {
        return if (value) 1 else 0
    }

}