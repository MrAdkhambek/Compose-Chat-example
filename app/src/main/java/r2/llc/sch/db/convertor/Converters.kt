package r2.llc.sch.db.convertor

import androidx.room.TypeConverter
import java.util.*


class Converters {

    @TypeConverter
    fun toLong(value: Long): Date = Date(value)

    @TypeConverter
    fun fromDate(value: Date): Long = value.time
}