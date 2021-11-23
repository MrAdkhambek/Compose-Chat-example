package r2.llc.sch.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import r2.llc.sch.db.convertor.Converters
import r2.llc.sch.db.entity.message.MessageDao
import r2.llc.sch.db.entity.message.MessageEntity


@Database(
    entities = [
        MessageEntity::class,
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val messageDao: MessageDao
}