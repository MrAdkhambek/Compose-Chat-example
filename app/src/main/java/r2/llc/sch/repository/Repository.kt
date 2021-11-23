package r2.llc.sch.repository

import androidx.paging.PagingSource
import r2.llc.sch.db.entity.message.MessageDao
import r2.llc.sch.db.entity.message.MessageEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Repository @Inject constructor(
    private val messageDao: MessageDao
) {

    fun getMessages(): PagingSource<Int, MessageEntity> = messageDao.getAllMessage()

    suspend fun saveMessages(messages: List<MessageEntity>) {
        messageDao.insert(messages)
    }

    suspend fun saveOwnMessages(userName: String, message: String) {
        val messageEntity = MessageEntity(
            message = message,
            senderName = userName,
            isOwn = true
        )
        messageDao.insert(messageEntity)
    }
}


