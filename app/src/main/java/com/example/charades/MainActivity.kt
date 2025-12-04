package com.example.charades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.charades.navigation.CharadesNavigation
import com.example.charades.ui.theme.CharadesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var inAppForeground by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                inAppForeground = true
            }
        }
        enableEdgeToEdge()
        setContent {
            CharadesTheme {
                CharadesNavigation(inAppForeground)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        inAppForeground = false
    }
}


@Preview
@Composable
fun AccelerometerTestPreview() {
    CharadesNavigation(true)
}
