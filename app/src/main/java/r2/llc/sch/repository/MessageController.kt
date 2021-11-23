package r2.llc.sch.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.serialization.builtins.serializer
import r2.llc.sch.db.entity.message.MessageEntity
import r2.llc.sch.util.JoinUserParser
import r2.llc.sch.util.LeftUserParser
import r2.llc.sch.util.MessageParser
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MessageController @Inject constructor(
    private val socketUtil: SocketUtil,
    private val repository: Repository
) {

    private val messageParser = MessageParser()
    private val joinUserParser = JoinUserParser()
    private val leftUserParser = LeftUserParser()

    val stateFlow: StateFlow<SocketUtil.Status>
        get() = socketUtil.stateFlow

    suspend fun start(coroutineScope: CoroutineScope) {
        socketUtil.connect()

        socketUtil
            .listenAsFlow("chat message")
            .map {
                it.mapNotNull { value ->
                    val parse = messageParser(value)
                    val msg = parse[MessageParser.MESSAGE] ?: return@mapNotNull null
                    val senderName = parse[MessageParser.USER_NAME] ?: return@mapNotNull null

                    MessageEntity(
                        message = msg,
                        senderName = senderName,
                        isOwn = false
                    )
                }
            }
            .onEach(repository::saveMessages)
            .launchIn(coroutineScope)

        socketUtil
            .listenAsFlow("is_online")
            .map {
                it.mapNotNull { value ->
                    val parseJoin = joinUserParser(value)
                    val parseLeft = leftUserParser(value)

                    val joinUserName = parseJoin[JoinUserParser.USER_NAME]
                    val leftUserName = parseLeft[LeftUserParser.USER_NAME]

                    val userName = joinUserName ?: leftUserName ?: return@mapNotNull null
                    val message = String.format("%s user %s", if (joinUserName.isNullOrBlank()) "Left" else "Join", userName)

                    MessageEntity(
                        message = message,
                        senderName = userName,
                        isOwn = false
                    )
                }
            }
            .onEach(repository::saveMessages)
            .launchIn(coroutineScope)
    }

    fun connect(userName: String) {
        socketUtil.emit("username", String.serializer(), userName)
    }

    suspend fun sendMessage(userName: String, message: String) {
        socketUtil.emit("chat message", String.serializer(), message)
        repository.saveOwnMessages(userName = userName, message = message)
    }

    fun onTyping() {
        socketUtil.emit("is_typing", Boolean.serializer(), true)
    }

    fun getMessages(): PagingSource<Int, MessageEntity> = repository.getMessages()
}