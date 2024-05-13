package br.univesp.pji610.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.univesp.pji610.database.dao.GroupIoTDAO
import br.univesp.pji610.database.dao.IoTDAO
import br.univesp.pji610.database.dao.NotaDao
import br.univesp.pji610.database.dao.RescueGroupDAO
import br.univesp.pji610.database.dao.UserDao
import br.univesp.pji610.database.migrations.MIGRATION_1_2
import br.univesp.pji610.database.migrations.MIGRATION_2_3
import br.univesp.pji610.database.migrations.MIGRATION_3_4
import br.univesp.pji610.database.model.GroupIoT
import br.univesp.pji610.database.model.IoT
import br.univesp.pji610.database.model.Nota
import br.univesp.pji610.database.model.RescueGroup
import br.univesp.pji610.database.model.User

@Database(
    version = 2,
    entities = [Nota::class, User::class, IoT::class, GroupIoT::class, RescueGroup::class],
    exportSchema = true
)
abstract class DataSource : RoomDatabase() {

    abstract fun notaDao(): NotaDao

    abstract fun userDao(): UserDao

    abstract fun iotTDAO(): IoTDAO

    abstract fun groupIoTDAO(): GroupIoTDAO

    abstract fun rescueGroupDAO(): RescueGroupDAO

    companion object {
        @Volatile
        private var db: DataSource? = null

        fun instance(context: Context): DataSource {
            return db ?: Room.databaseBuilder(
                context,
                DataSource::class.java,
                "pji610.db"
            )
            .fallbackToDestructiveMigration()
            .build()
        }
    }

}

//.addMigrations(
//MIGRATION_1_2,
//MIGRATION_2_3,
//MIGRATION_3_4