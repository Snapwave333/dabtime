package com.dabtime.app.ui.screens.timer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dabtime.app.data.model.HeatZone
import com.dabtime.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    onNavigateBack: () -> Unit,
    viewModel: TimerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Text(
                text = "Dab Timer",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            
            // Spacer to balance the back button
            Spacer(modifier = Modifier.width(48.dp))
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Timer Display
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatTime(uiState.timeRemaining),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = getTimerColor(uiState.timeRemaining),
                fontSize = 72.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Heat Status
            Text(
                text = getHeatStatusText(uiState.currentHeatZone, uiState.timeRemaining),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Thermometer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            ThermometerView(
                currentHeatZone = uiState.currentHeatZone,
                timeRemaining = uiState.timeRemaining,
                totalTime = uiState.totalTime
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Timer Mode Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Timer Mode",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TimerModeButton(
                        text = "Strict",
                        isSelected = uiState.timerMode.name == "STRICT",
                        onClick = { viewModel.setTimerMode(com.dabtime.app.data.model.TimerMode.STRICT) }
                    )
                    TimerModeButton(
                        text = "Balanced",
                        isSelected = uiState.timerMode.name == "BALANCED",
                        onClick = { viewModel.setTimerMode(com.dabtime.app.data.model.TimerMode.BALANCED) }
                    )
                    TimerModeButton(
                        text = "Chill",
                        isSelected = uiState.timerMode.name == "CHILL",
                        onClick = { viewModel.setTimerMode(com.dabtime.app.data.model.TimerMode.CHILL) }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Control Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Reset Button
            OutlinedButton(
                onClick = { viewModel.resetTimer() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Reset")
            }
            
            // Start/Pause Button
            Button(
                onClick = { viewModel.toggleTimer() },
                modifier = Modifier.weight(2f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isRunning) DabOrange else DabGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = if (uiState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (uiState.isRunning) "Pause" else "Start",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (uiState.isRunning) "Pause" else "Start",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TimerModeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) DabGreen else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) DabWhite else MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(36.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ThermometerView(
    currentHeatZone: HeatZone,
    timeRemaining: Int,
    totalTime: Int
) {
    Canvas(
        modifier = Modifier
            .width(60.dp)
            .height(280.dp)
            .clip(RoundedCornerShape(30.dp))
    ) {
        drawThermometer(
            currentHeatZone = currentHeatZone,
            timeRemaining = timeRemaining,
            totalTime = totalTime
        )
    }
}

fun DrawScope.drawThermometer(
    currentHeatZone: HeatZone,
    timeRemaining: Int,
    totalTime: Int
) {
    val width = size.width
    val height = size.height
    val borderWidth = 4.dp.toPx()
    
    // Draw thermometer background
    drawRect(
        color = ThermometerBackground,
        topLeft = Offset(borderWidth, borderWidth),
        size = androidx.compose.ui.geometry.Size(
            width - borderWidth * 2,
            height - borderWidth * 2
        )
    )
    
    // Draw thermometer border
    drawRect(
        color = ThermometerBorder,
        topLeft = Offset(0f, 0f),
        size = androidx.compose.ui.geometry.Size(width, height),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = borderWidth)
    )
    
    // Calculate fill height based on time remaining
    val progress = if (totalTime > 0) {
        (totalTime - timeRemaining).toFloat() / totalTime.toFloat()
    } else 0f
    
    val fillHeight = (height - borderWidth * 2) * progress.coerceIn(0f, 1f)
    
    // Draw heat zone color fill
    val fillColor = when (currentHeatZone) {
        HeatZone.NOT_READY -> HeatNotReady
        HeatZone.LOW_TEMP -> HeatLowTemp
        HeatZone.MEDIUM_TEMP -> HeatMediumTemp
        HeatZone.HIGH_TEMP -> HeatHighTemp
    }
    
    if (fillHeight > 0) {
        drawRect(
            color = fillColor,
            topLeft = Offset(borderWidth, height - borderWidth - fillHeight),
            size = androidx.compose.ui.geometry.Size(
                width - borderWidth * 2,
                fillHeight
            )
        )
    }
}

fun formatTime(seconds: Int): String {
    val abs = kotlin.math.abs(seconds)
    val mins = abs / 60
    val secs = abs % 60
    val sign = if (seconds < 0) "-" else ""
    return "$sign$mins:${secs.toString().padStart(2, '0')}"
}

fun getTimerColor(timeRemaining: Int): Color {
    return if (timeRemaining < 0) {
        DabRed // Negative time (overtime)
    } else {
        DabGreen // Normal time
    }
}

fun getHeatStatusText(heatZone: HeatZone, timeRemaining: Int): String {
    return when {
        timeRemaining < 0 -> "🕒 Time since dab: ${formatTime(kotlin.math.abs(timeRemaining))}"
        heatZone == HeatZone.HIGH_TEMP -> "🔥 High Temp Dab Ready"
        heatZone == HeatZone.MEDIUM_TEMP -> "🌡️ Medium Temp Dab Ready"
        heatZone == HeatZone.LOW_TEMP -> "❄️ Low Temp Dab Ready"
        heatZone == HeatZone.NOT_READY -> "⏱️ Dab Not Ready"
        else -> ""
    }
}