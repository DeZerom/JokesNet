package ru.dezerom.jokesnet.db.profile

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileInfoDao {

    @Query("SELECT * FROM ProfileInfo LIMIT 1")
    fun selectProfileInfo(): DbProfileInfo?

    @Insert
    fun insertProfileInfo(profileInfo: DbProfileInfo)

    @Update
    fun updateProfileInfo(profileInfo: DbProfileInfo)

}