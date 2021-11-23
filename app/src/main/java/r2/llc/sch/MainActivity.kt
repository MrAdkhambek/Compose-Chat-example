package r2.llc.sch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import r2.llc.sch.ui.screen.auth.AuthScreen
import r2.llc.sch.theme.AppTheme
import r2.llc.sch.ui.screen.main.MainScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Navigator(AuthScreen())
            }
        }
    }
}