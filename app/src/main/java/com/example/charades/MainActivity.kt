package com.example.charades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.charades.navigation.CharadesNavigation
import com.example.charades.ui.theme.CharadesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharadesTheme {
                CharadesNavigation()
            }
        }
    }
}


@Preview
@Composable
fun AccelerometerTestPreview() {
    CharadesNavigation()
}


