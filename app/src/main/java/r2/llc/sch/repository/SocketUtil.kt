package r2.llc.sch.repository

import android.util.Log
import io.socket.client.Ack
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SocketUtil @Inject constructor(
    private val socketIO: Socket,
    private val json: Json,
) {

    private val _stateFlow: MutableStateFlow<Status> = MutableStateFlow(Status.DISCONNECTED)
    val stateFlow: StateFlow<Status> = _stateFlow.asStateFlow()

    fun connect() {
        socketIO.connect()
    }

    fun disconnect() {
        socketIO.disconnect()
    }

    fun <T> emit(event: String, serializer: SerializationStrategy<T>, value: T) {
        socketIO.emit(event, json.encodeToJsonElement(serializer, value))
    }

    fun <T> emit(event: String, serializer: SerializationStrategy<T>, value: T, ack: Ack) {
        socketIO.emit(event, json.encodeToJsonElement(serializer, value), ack)
    }

    init {
        socketIO.on(Socket.EVENT_CONNECT) {
            Log.d("SocketUtil: CONNECT", it?.firstOrNull().toString())
            _stateFlow.value = Status.CONNECTED
        }

        socketIO.on(Socket.EVENT_DISCONNECT) {
            Log.d("SocketUtil: DISCONNECT", it?.firstOrNull().toString())
            _stateFlow.value = Status.DISCONNECTED
        }

        socketIO.on(Socket.EVENT_CONNECT_ERROR) {
            Log.d("SocketUtil: _ERROR", it?.firstOrNull().toString())
            _stateFlow.value = Status.CONNECT_ERROR
        }
    }

    fun <T> eventFlow(event: String, contentSerializer: KSerializer<T>): Flow<List<T>> = callbackFlow {
        val listener = Emitter.Listener { args ->
            require(this.trySend(parse(event, args, contentSerializer)).isSuccess)
        }

        socketIO.on(event, listener)
        awaitClose { socketIO.off(event, listener) }
    }

    fun listenAsFlow(event: String): Flow<List<String>> = callbackFlow {
        val listener = Emitter.Listener { args ->
            val list = args.map { it.toString() }
            require(this.trySend(list).isSuccess)
        }

        socketIO.on(event, listener)
        awaitClose { socketIO.off(event, listener) }
    }

    private fun <T> parse(event: String, args: Array<out Any>, contentSerializer: KSerializer<T>): List<T> {
        return args.mapNotNull { argument ->
            try {
                json.decodeFromString(contentSerializer, argument.toString())
            } catch (e: Exception) {
                null
            }
        }
    }

    enum class Status {
        CONNECTED,
        DISCONNECTED,
        CONNECT_ERROR
    }
}