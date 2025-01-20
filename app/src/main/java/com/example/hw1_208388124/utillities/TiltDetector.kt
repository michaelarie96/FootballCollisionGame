package com.example.hw1_208388124.utillities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.hw1_208388124.interfaces.TiltCallback

class TiltDetector(
    context: Context,
    private val tiltCallback: TiltCallback?
    ) {
        private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        private var lastTiltTime = 0L
        private val TILT_THRESHOLD = 3.0f
        private val TILT_COOLDOWN = 250L

        private val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (System.currentTimeMillis() - lastTiltTime < TILT_COOLDOWN) {
                    return
                }

                val x = event.values[0]  // Left/Right tilt
                val y = event.values[1]  // Forward/Backward tilt

                when {
                    x >= TILT_THRESHOLD -> {
                        tiltCallback?.onTiltLeft()
                        lastTiltTime = System.currentTimeMillis()
                    }
                    x <= -TILT_THRESHOLD -> {
                        tiltCallback?.onTiltRight()
                        lastTiltTime = System.currentTimeMillis()
                    }
                }

                when {
                    y >= TILT_THRESHOLD -> {
                        tiltCallback?.onTiltBackward()
                        lastTiltTime = System.currentTimeMillis()
                    }
                    y <= -TILT_THRESHOLD -> {
                        tiltCallback?.onTiltForward()
                        lastTiltTime = System.currentTimeMillis()
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Not needed
            }
        }

        fun start() {
            sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        fun stop() {
            sensorManager.unregisterListener(
                sensorEventListener,
                sensor
            )
        }
}