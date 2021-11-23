package r2.llc.sch.db.entity.message


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "message_table")
data class MessageEntity(

    @ColumnInfo(name = "message")
    val message: String,

    @ColumnInfo(name = "sender_name")
    val senderName: String,

    @ColumnInfo(name = "is_own")
    val isOwn: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long? = null
}