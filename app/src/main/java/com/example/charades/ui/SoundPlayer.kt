package com.example.charades.ui

import android.media.RingtoneManager
import android.view.SoundEffectConstants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

@Composable
fun PlaySuccessSound() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val ringtone = try {
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            RingtoneManager.getRingtone(context, uri)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        ringtone?.play()

        onDispose {
            if (ringtone?.isPlaying == true) {
                ringtone.stop()
            }
        }
    }
}

@Composable
fun PlayCancelSound() {
    val view = LocalView.current
    LaunchedEffect(Unit) {
        view.playSoundEffect(SoundEffectConstants.CLICK)
    }
}
