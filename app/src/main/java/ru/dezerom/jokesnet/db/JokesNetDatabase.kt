package ru.dezerom.jokesnet.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.dezerom.jokesnet.db.converters.BooleanIntConverter
import ru.dezerom.jokesnet.db.joke.DbJoke
import ru.dezerom.jokesnet.db.joke.JokeDao
import ru.dezerom.jokesnet.db.profile.DbProfileInfo
import ru.dezerom.jokesnet.db.profile.ProfileInfoDao

@Database(
    entities = [DbProfileInfo::class, DbJoke::class],
    version = 3,
    autoMigrations = [AutoMigration(from = 2, to = 3)],
    exportSchema = true
)
@TypeConverters(value = [BooleanIntConverter::class])
abstract class JokesNetDatabase: RoomDatabase() {
    abstract fun getProfileInfo(): ProfileInfoDao
    abstract fun getJokeDao(): JokeDao

    companion object {
        val MIGRATION_1_2 = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS token")
                database.execSQL("CREATE TABLE IF NOT EXISTS ProfileInfo(\nid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\nlogin VARCHAR NOT NULL,\njokesAdded INTEGER NOT NULL,\nisValid INTEGER NOT NULL\n)")
            }
        }
    }
}