package com.example.charades.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

data class TiltData(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
)


class AccelerometerManager (context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    var onTiltChanged: ((TiltData) -> Unit)? = null

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                // Create TiltData with the three values
                val data = TiltData(
                    x = it.values[0],
                    y = it.values[1],
                    z = it.values[2]
                )
                // Call our function with the new data
                onTiltChanged?.invoke(data)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not used
        }
    }

    // Start listening to the sensor
    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(
                sensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_GAME  // Update speed
            )
        }
    }

    // Stop listening (important to save battery!)
    fun stop() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}

@Composable
fun rememberAccelerometer(): TiltData {
    val context = LocalContext.current
    var tiltData by remember { mutableStateOf(TiltData()) }

    DisposableEffect(Unit) {
        val accelManager = AccelerometerManager(context)

        accelManager.onTiltChanged = { data ->
            tiltData = data
        }

        accelManager.start()

        onDispose {
            accelManager.stop()
        }
    }

    return tiltData
}
