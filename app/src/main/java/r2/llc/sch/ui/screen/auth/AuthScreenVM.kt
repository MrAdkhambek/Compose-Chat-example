package r2.llc.sch.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthScreenVM  @Inject constructor(

): ViewModel() {

    private val _inputState: MutableStateFlow<String> = MutableStateFlow("")
    val inputState: StateFlow<String> get() = _inputState.asStateFlow()

    fun onEditName(name: String) {
        viewModelScope.launch {
            _inputState.emit(name)
        }
    }
}