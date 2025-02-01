package com.pizzy.ptilms

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pizzy.ptilms.data.local.AnnouncementDao
import com.pizzy.ptilms.data.local.AssignmentDao
import com.pizzy.ptilms.data.local.ChatDao
import com.pizzy.ptilms.data.local.ChatMessageDao
import com.pizzy.ptilms.data.local.CourseDao
import com.pizzy.ptilms.data.local.DepartmentDao
import com.pizzy.ptilms.data.local.DepartmentLevelDao
import com.pizzy.ptilms.data.local.LevelDao
import com.pizzy.ptilms.data.local.UserDao
import com.pizzy.ptilms.data.model.AnnouncementEntity
import com.pizzy.ptilms.data.model.AssignmentEntity
import com.pizzy.ptilms.data.model.ChatEntity
import com.pizzy.ptilms.data.model.ChatMessageEntity
import com.pizzy.ptilms.data.model.CourseEntity
import com.pizzy.ptilms.data.model.DepartmentEntity
import com.pizzy.ptilms.data.model.DepartmentLevelCrossRef
import com.pizzy.ptilms.data.model.LevelEntity
import com.pizzy.ptilms.data.model.UserCourseCrossRef
import com.pizzy.ptilms.data.model.UserEntity
import com.pizzy.ptilms.utils.Converters
import timber.log.Timber

@Database(
    entities = [
        DepartmentEntity::class,
        LevelEntity::class,
        CourseEntity::class,
        UserEntity::class,
        AnnouncementEntity::class,
        AssignmentEntity::class,
        ChatMessageEntity::class,
        ChatEntity::class,
        UserCourseCrossRef::class,
        DepartmentLevelCrossRef::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun departmentDao(): DepartmentDao
    abstract fun levelDao(): LevelDao
    abstract fun courseDao(): CourseDao
    abstract fun userDao(): UserDao
    abstract fun announcementDao(): AnnouncementDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun chatDao(): ChatDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun departmentLevelDao(): DepartmentLevelDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Timber.d("Migrating from version 1 to 2")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add index for username to improve lookup performance
                Timber.d("Migrating from version 2 to 3: Adding index on users.username")
                database.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_users_username ON users(username)"
                )
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ptilms_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // Add all migrations here
                    // .fallbackToDestructiveMigration() // Removed for production: data loss is unacceptable
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Timber.d("Database created")
                        }
                    })
                    .setJournalMode(JournalMode.TRUNCATE)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}