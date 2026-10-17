package com.charlesmuchogo.research.presentation.testpage

import android.graphics.BitmapFactory
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.charlesmuchogo.research.R
import com.charlesmuchogo.research.navController
import com.charlesmuchogo.research.navigation.DoctorsPage
import com.charlesmuchogo.research.presentation.common.AppAlertDialog
import com.charlesmuchogo.research.presentation.messages.components.SurfaceCard

const val TOTAL_TEST_STEPS = 5

fun currentTestStep(started: Boolean, timerDone: Boolean, photosReady: Boolean): Int = when {
    !started -> 2
    !timerDone -> 3
    !photosReady -> 4
    else -> 5
}

@Composable
private fun stepLabels(): List<String> = listOf(
    stringResource(R.string.step_prepare_kit),
    stringResource(R.string.step_collect_sample),
    stringResource(R.string.step_wait),
    stringResource(R.string.step_photograph),
    stringResource(R.string.step_submit),
)

private fun countdown(milliseconds: Long): String {
    val totalSeconds = (milliseconds.coerceAtLeast(0L) / 1000)
    return "%02d:%02d".format(totalSeconds / 60, totalSeconds % 60)
}

@Composable
fun TestTimerCard(
    step: Int,
    started: Boolean,
    timeSpent: Long,
    totalDuration: Long,
    onTimerClick: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showCancelDialog by remember { mutableStateOf(false) }

    if (showCancelDialog) {
        AppAlertDialog(
            onDismissRequest = { showCancelDialog = false },
            onConfirmation = {
                showCancelDialog = false
                onCancel()
            },
            dialogTitle = stringResource(R.string.cancel_timer_title),
            dialogText = stringResource(R.string.cancel_timer_body),
            confirmContent = { Text(stringResource(R.string.cancel_timer)) },
        )
    }

    val timerDone = started && timeSpent >= totalDuration
    val progress by animateFloatAsState(
        targetValue = if (totalDuration > 0) (timeSpent.toFloat() / totalDuration).coerceIn(0f, 1f) else 0f,
        animationSpec = tween(800),
        label = "timer",
    )
    val track = MaterialTheme.colorScheme.primaryContainer
    val accent = MaterialTheme.colorScheme.primary

    SurfaceCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = stringResource(R.string.test_step_of, step, TOTAL_TEST_STEPS, stepLabels()[step - 1]).uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.6.sp),
                color = accent,
                modifier = Modifier.fillMaxWidth(),
            )
            Box(
                modifier = Modifier
                    .size(188.dp)
                    .clip(CircleShape)
                    .clickable(enabled = !started, role = Role.Button, onClick = onTimerClick),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(176.dp)) {
                    val stroke = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                    drawArc(color = track, startAngle = 0f, sweepAngle = 360f, useCenter = false, style = stroke)
                    drawArc(color = accent, startAngle = -90f, sweepAngle = 360f * progress, useCenter = false, style = stroke)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = countdown(if (started) totalDuration - timeSpent else totalDuration),
                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold),
                    )
                    Text(
                        text = stringResource(if (started) R.string.timer_remaining else R.string.start_timer),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = if (started) MaterialTheme.colorScheme.onSurfaceVariant else accent,
                    )
                }
            }
            Text(
                text = stringResource(
                    when {
                        !started -> R.string.timer_hint_idle
                        !timerDone -> R.string.timer_hint_running
                        else -> R.string.timer_hint_done
                    }
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            if (!started) {
                Button(
                    onClick = onTimerClick,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                ) {
                    Text(stringResource(R.string.start_timer), fontWeight = FontWeight.Bold)
                }
            } else {
                TextButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.heightIn(min = 48.dp),
                ) {
                    Text(
                        text = stringResource(R.string.cancel_timer),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
    }
}

@Composable
fun TestStepsList(currentStep: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        stepLabels().forEachIndexed { index, label ->
            val number = index + 1
            val done = number < currentStep
            val current = number == currentStep
            val shape = MaterialTheme.shapes.small
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(if (current) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
                    .border(
                        1.dp,
                        if (current) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant,
                        shape,
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .then(
                            when {
                                done -> Modifier.background(MaterialTheme.colorScheme.tertiaryContainer)
                                current -> Modifier.background(MaterialTheme.colorScheme.primary)
                                else -> Modifier.border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
                            }
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    if (done) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.step_done),
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(16.dp),
                        )
                    } else {
                        Text(
                            text = number.toString(),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = if (current) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (current) FontWeight.Bold else FontWeight.SemiBold),
                    color = when {
                        current -> MaterialTheme.colorScheme.onPrimaryContainer
                        done -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                )
            }
        }
    }
}

@Composable
fun TestPhotoSlot(
    image: ByteArray?,
    label: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val outline = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val bitmap = remember(image) { image?.let { BitmapFactory.decodeByteArray(it, 0, it.size)?.asImageBitmap() } }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .drawBehind {
                drawRoundRect(
                    color = outline,
                    cornerRadius = CornerRadius(20.dp.toPx()),
                    style = Stroke(width = 1.5.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), 0f)),
                )
            }
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = label,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(if (enabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = null,
                        tint = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun AskDoctorLink(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable(role = Role.Button, onClick = { navController.navigate(DoctorsPage) })
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.MedicalServices,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = stringResource(R.string.ask_doctor_step),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 6.dp),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp),
        )
    }
}
