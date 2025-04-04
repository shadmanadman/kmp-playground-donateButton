package org.kmp.playground.donate.button

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun DonateButton() {
    val coroutineScope = rememberCoroutineScope()

    val buttonWidth = 280.dp
    val buttonHeight = 140.dp

    var isToggled by remember { mutableStateOf(false) }
    var amountOfDonate by remember { mutableStateOf(0.0) }

    val buttonCornerRadius = if (isToggled) 0.dp else 25.dp
    val animatedCircleOffset by animateDpAsState(
        targetValue = buttonCornerRadius,
        animationSpec = tween(durationMillis = 1000, easing = EaseInBounce),
        label = "offset animation"
    )

    val textAlpha = if (isToggled) 0f else 1f
    val animatedTextAlpha by animateFloatAsState(
        targetValue = textAlpha,
        animationSpec = tween(durationMillis = 800, easing = EaseIn),
        label = "offset animation"
    )
    val flapRotation = remember { Animatable(0f) }


    // Envelope Flap
    Canvas(
        modifier = Modifier
            .size(265.dp, 100.dp)
            .offset(y = (155).dp)
            .graphicsLayer {
                rotationX = flapRotation.value
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(
                    0.5f,
                    0f
                )
                translationY = -50.dp.toPx()
            }
    ) {
        val width = size.width
        val triangleHeight = width * 0.3f

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(width, 0f)
            lineTo(width / 2f, triangleHeight)
            close()
        }
        drawPath(path = path, color = Color.White)
    }
    var donateText by remember { mutableStateOf("Donate") }
    Box(
        modifier = Modifier.size(buttonWidth, buttonHeight)
            .clip(RoundedCornerShape(animatedCircleOffset))
            .background(Color.White)
            .zIndex(2f)
            .clickable(onClick = {
                isToggled = !isToggled

                coroutineScope.launch {
                    if (!isToggled) {
                        flapRotation.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(
                                durationMillis = 800,
                                easing = LinearEasing
                            )
                        )
                    } else {
                        flapRotation.animateTo(
                            targetValue = -180f, // Rotate downwards
                            animationSpec = tween(
                                durationMillis = 800,
                                easing = LinearEasing
                            )
                        )
                        donateText = "Thank You"
                    }
                }
            })
    ) {

        Text(
            donateText,
            modifier = Modifier.alpha(animatedTextAlpha).align(Alignment.Center),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }


    val scale = remember { Animatable(1f) }

    val donateRectX = if (isToggled) 190.dp else 0.dp
    val animationDelay = if (isToggled) 800 else 0
    val animationDuration = if (isToggled) 1500 else 800
    val animatedDonateRectXOffset by animateDpAsState(
        targetValue = donateRectX,
        animationSpec = tween(durationMillis = animationDuration, delayMillis = animationDelay, easing = EaseInOut),
        label = "offset animation"
    )

    Box(
        modifier = Modifier
            .size(buttonWidth, 100.dp)
            .scale(scale.value, scale.value)
            .offset(y = (-(120 + animatedDonateRectXOffset.value)).dp)
            .background(Color.Blue)
            .clickable(onClick = {
                amountOfDonate += 10
                coroutineScope.launch {
                    scale.animateTo(1.2f, animationSpec = tween(durationMillis = 150))
                    scale.animateTo(
                        1f,
                        animationSpec = SpringSpec(dampingRatio = 0.7f, stiffness = 200f)
                    )
                }
            }).drawBehind {
                val strokeWidth = 10f
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color.White,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }, contentAlignment = Alignment.Center
    ) {
        Text("${amountOfDonate}$", fontSize = 24.sp, color = Color.White)
    }
}
