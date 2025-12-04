package com.example.charades.ui

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.charades.R

@Composable
fun CorrectView(soundEnabled: Boolean) {
    val mContext = LocalContext.current

    if (soundEnabled) {
        // Correctly handle MediaPlayer lifecycle
        DisposableEffect(Unit) {
            val mMediaPlayer = MediaPlayer.create(mContext, R.raw.correct)
            mMediaPlayer.start()

            onDispose {
                if (mMediaPlayer.isPlaying) {
                    mMediaPlayer.stop()
                }
                mMediaPlayer.release()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.greenbg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()

        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "✓",
                fontSize = 64.sp,
                color = Color.White
            )
            Text(
                text = "Teisingai",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Preview(
    name = "Horizontal Preview",
    widthDp = 800, // Tipinis horizontalus plotis
    heightDp = 360 // Tipinis horizontalus aukštis
)
@Composable
fun CorrectViewPreview(){
    CorrectView(soundEnabled = true)
}
