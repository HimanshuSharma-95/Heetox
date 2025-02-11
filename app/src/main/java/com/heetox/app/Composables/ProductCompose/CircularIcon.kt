package com.heetox.app.Composables.ProductCompose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun IconWithCircularProgress(
    modifier: Modifier = Modifier,
    icon: Painter,           // The icon to display
    progress: Float,             // Progress value between 0 and 100
    borderColor: Color = Color.Green,
    borderWidth: Dp = 4.dp,      // Width of the circular border
    iconSize: Dp = 40.dp,        // Size of the icon
    backgroundColor: Color = Color.White // Background color inside the circle
) {
    Box(
        modifier = modifier
            .size(iconSize + borderWidth * 7) // Adjust size to include border
                       // Padding to center the icon within the border
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val sweepAngle = (progress / 100f) * 360f
            val diameter = size.minDimension
            val radius = diameter / 2

            // Draw the circular border with the progress
            drawArc(
                color = borderColor,
                startAngle = -90f, // Start from the top
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = borderWidth.toPx(), cap = StrokeCap.Round),
                size = Size(diameter, diameter),
                topLeft = Offset((size.width - diameter) / 2, (size.height - diameter) / 2)
            )

            // Draw the background circle (optional)
            drawCircle(
                color = backgroundColor,
                radius = radius - borderWidth.toPx() / 2,
                center = Offset(size.width / 2, size.height / 2)
            )
        }

        // Draw the icon centered in the circular border
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            modifier = Modifier
//                .size(iconSize)
//                .align(Alignment.Center)
//            ,
//            tint = Color.Black // Change the color of the icon as needed
//        )

        Image(painter = icon,
            contentDescription = "",
            modifier = Modifier
                .size(iconSize)
                .align(Alignment.Center)
            )
    }
}