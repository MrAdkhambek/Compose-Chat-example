package r2.llc.sch.db.entity.message

import androidx.paging.PagingSource
import androidx.room.*


@Dao
interface MessageDao {

    @Query("""
        SELECT * FROM message_table AS mt
        ORDER BY mt._id DESC
    """)
    fun getAllMessage(): PagingSource<Int, MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(messageEntity: List<MessageEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(messageEntity: MessageEntity): Long

    @Query("DELETE FROM message_table")
    suspend fun clearAll()
}