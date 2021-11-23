package r2.llc.sch.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import r2.llc.sch.db.entity.message.MessageEntity
import r2.llc.sch.repository.MessageController
import javax.inject.Inject


@HiltViewModel
class MainScreenVM @Inject constructor(
    private val messageController: MessageController
) : ViewModel() {

    private val _inputState: MutableStateFlow<String> = MutableStateFlow("")
    val inputState: StateFlow<String> get() = _inputState.asStateFlow()

    val messages: Flow<PagingData<MessageEntity>>
        get() {
            return Pager(
                config = getDefaultPageConfig(),
                pagingSourceFactory = messageController::getMessages,
            ).flow
        }

    fun connect(userName: String) {
        viewModelScope.launch {
            messageController.start(viewModelScope)
            messageController.connect(userName)
        }
    }

    fun sendMessage(userName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            messageController.sendMessage(userName = userName, message = inputState.value)
            _inputState.emit("")
        }
    }

    fun onEditMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _inputState.emit(message)
            messageController.onTyping()
        }
    }

    init {
        messageController
            .stateFlow
            .onEach {
                Log.d("TTT", it.name)
            }.launchIn(viewModelScope)
    }

    /**
     * let's define page size, page size is the only required param, rest is optional
     */
    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }
}