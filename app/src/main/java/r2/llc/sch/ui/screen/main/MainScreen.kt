package r2.llc.sch.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import r2.llc.sch.theme.Purple200
import r2.llc.sch.theme.Teal200


class MainScreen(
    private val userName: String
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val vm = getViewModel<MainScreenVM>()
        val inputState by vm.inputState.collectAsState()
        vm.connect(userName = userName)

        val messages = vm.messages.collectAsLazyPagingItems()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text("Socket.IO chat") })
            },
            bottomBar = {
                BottomAppBar {
                    BasicTextField(
                        value = inputState,
                        onValueChange = vm::onEditMessage,
                        maxLines = 2,
                        textStyle = TextStyle(color = Color.White),
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        modifier = Modifier.background(Color.Gray),
                        onClick = { vm.sendMessage(userName) }) {
                        Text(text = "Inter")
                    }
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                reverseLayout = true,
            ) {
                itemsIndexed(items = messages, itemContent = { position, item ->
                    Text(
                        text = item?.message ?: "Message",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (position % 2 == 0) Teal200 else Purple200)
                    )
                })
            }
        }
    }
}