package r2.llc.sch.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import r2.llc.sch.ui.screen.main.MainScreen


class AuthScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val vm = getViewModel<AuthScreenVM>()
        val inputState by vm.inputState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),

            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Name")

                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp))

                    TextField(
                        value = inputState,
                        onValueChange = vm::onEditName,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row {
                        TextButton(onClick = {
                            navigator.replace(MainScreen(inputState))
                        }) {
                            Text(text = "Enter")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ContentPreview() {
    AuthScreen().Content()
}