package com.tlajja.charades.audio

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.tlajja.charades.R

class SoundManager(context: Context) {
    private var correctPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.correct)
    private var skipPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.skip)
    private var gameEndPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.gameend)

    fun playCorrect() {
        try {
            correctPlayer?.let { player ->
                if (player.isPlaying) {
                    player.seekTo(0)
                } else {
                    player.start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playSkip() {
        try {
            skipPlayer?.let { player ->
                if (player.isPlaying) {
                    player.seekTo(0)
                } else {
                    player.start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playGameEnd() {
        try {
            gameEndPlayer?.let { player ->
                if (player.isPlaying) {
                    player.seekTo(0)
                } else {
                    player.start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopAll() {
        try {
            correctPlayer?.let { if (it.isPlaying) it.pause() }
            skipPlayer?.let { if (it.isPlaying) it.pause() }
            gameEndPlayer?.let { if (it.isPlaying) it.pause() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        try {
            correctPlayer?.release()
            skipPlayer?.release()
            gameEndPlayer?.release()
            correctPlayer = null
            skipPlayer = null
            gameEndPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun rememberSoundManager(): SoundManager {
    val context = LocalContext.current
    val soundManager = remember { SoundManager(context) }

    DisposableEffect(Unit) {
        onDispose {
            soundManager.release()
        }
    }

    return soundManager
}