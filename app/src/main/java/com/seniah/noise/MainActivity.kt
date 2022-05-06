package com.seniah.noise

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.seniah.noise.SimplexNoise.noise
import com.seniah.noise.ui.theme.NoiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoiseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SimplexNoiseView()
                    // PerlinNoiseView()
                    // RandomNoiseView()
                }
            }
        }
    }
}

@Composable
fun SimplexNoiseView() {
    val infiniteTransition = rememberInfiniteTransition()
    val zPosition by infiniteTransition.animateFloat(
        initialValue = 0.01F,
        targetValue = 0.9F,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val screenWidth = size.width.toInt()
        val screenHeight = size.height.toInt()
        val boxSize = 25
        val scale = 0.02

        for (y in 0..screenHeight step boxSize) {
            for (x in 0..screenWidth step boxSize) {
                val xPosition = (x / boxSize) * scale
                val yPosition = (y / boxSize) * scale
                val noiseResult = noise(xPosition, yPosition, zPosition.toDouble())
                val noiseColour = (noiseResult * 1000).toInt()

                drawRect(
                    topLeft = Offset(x.toFloat(), y.toFloat()),
                    color = Color(noiseColour, noiseColour, noiseColour, 0xFF)
                )
            }
        }
    }
}

@Composable
fun PerlinNoiseView() {
    val infiniteTransition = rememberInfiniteTransition()
    val zPosition by infiniteTransition.animateFloat(
        initialValue = 0.01F,
        targetValue = 0.9F,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val perlin = Perlin()
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val screenWidth = size.width.toInt()
        val screenHeight = size.height.toInt()
        val boxSize = 25
        val scale = 0.02

        for (y in 0..screenHeight step boxSize) {
            for (x in 0..screenWidth step boxSize) {
                val xPosition = (x / boxSize) * scale
                val yPosition = (y / boxSize) * scale
                val noiseResult = perlin.noise(xPosition, yPosition, zPosition.toDouble())
                val noiseColour = (noiseResult * 1000).toInt()

                drawRect(
                    topLeft = Offset(x.toFloat(), y.toFloat()),
                    color = Color(noiseColour, noiseColour, noiseColour, 0xFF)
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RandomNoiseView() {
    var invalidations by remember {
        mutableStateOf(0)
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        invalidations++
                    }
                    MotionEvent.ACTION_MOVE -> {}
                    MotionEvent.ACTION_UP -> {}
                }
                true
            }
    ) {
        val screenWidth = size.width.toInt()
        val screenHeight = size.height.toInt()
        val boxSize = 25

        invalidations.let {
            for (y in 0..screenHeight step boxSize) {
                for (x in 0..screenWidth step boxSize) {
                    val randomColour = (0..0xFF).random()
                    drawRect(
                        topLeft = Offset(x.toFloat(), y.toFloat()),
                        color = Color(randomColour, randomColour, randomColour, 0xFF)
                    )
                }
            }
        }
    }
}
