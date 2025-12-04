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

data class GyroscopeData(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
)

@Composable
fun rememberGyroscope(inAppForeground: Boolean): GyroscopeData {
    val context = LocalContext.current
    var gyroscopeData by remember { mutableStateOf(GyroscopeData()) }

    DisposableEffect(inAppForeground) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (it.sensor.type == Sensor.TYPE_GYROSCOPE) {
                        gyroscopeData = GyroscopeData(
                            x = it.values[0],
                            y = it.values[1],
                            z = it.values[2]
                        )
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not used
            }
        }
        if (inAppForeground) {
            sensorManager.registerListener(
                sensorEventListener,
                gyroscope,
                SensorManager.SENSOR_DELAY_UI
            )
        }
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    return gyroscopeData
}
