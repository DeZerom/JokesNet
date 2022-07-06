package ru.dezerom.jokesnet.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.dezerom.jokesnet.db.profile.DbProfileInfo
import ru.dezerom.jokesnet.db.profile.ProfileInfoDao
import ru.dezerom.jokesnet.db.token.Token
import ru.dezerom.jokesnet.db.token.TokenDao
import ru.dezerom.jokesnet.screens.profile.ProfileInfo

@Database(entities = [DbProfileInfo::class], version = 2, exportSchema = true)
abstract class JokesNetDatabase: RoomDatabase() {
    abstract fun getProfileInfo(): ProfileInfoDao

    companion object {
        val MIGRATION_1_2 = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS token")
                database.execSQL("CREATE TABLE IF NOT EXISTS ProfileInfo(\nid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\nlogin VARCHAR NOT NULL,\njokesAdded INTEGER NOT NULL\n)")
            }
        }
    }
}