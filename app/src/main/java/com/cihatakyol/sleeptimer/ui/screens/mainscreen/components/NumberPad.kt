package com.cihatakyol.sleeptimer.ui.screens.mainscreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NumberPad(
    onNumberClick: (Int) -> Unit,
    onRemoveClick: () -> Unit,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    isActive: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NumberButton(number = 1, onClick = onNumberClick)
            NumberButton(number = 2, onClick = onNumberClick)
            NumberButton(number = 3, onClick = onNumberClick)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NumberButton(number = 4, onClick = onNumberClick)
            NumberButton(number = 5, onClick = onNumberClick)
            NumberButton(number = 6, onClick = onNumberClick)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NumberButton(number = 7, onClick = onNumberClick)
            NumberButton(number = 8, onClick = onNumberClick)
            NumberButton(number = 9, onClick = onNumberClick)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NumberButton(number = 0, onClick = onNumberClick)
            IconButton(
                modifier = Modifier.size(64.dp),
                onClick = onRemoveClick
            ) {
                Icon(
                    Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = ""
                )
            }
            if (isActive) {
                IconButton(
                    modifier = Modifier.size(64.dp),
                    onClick = onStopClick
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = ""
                    )
                }
            } else {
                IconButton(
                    modifier = Modifier.size(64.dp),
                    onClick = onStartClick
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
private fun NumberButton(
    number: Int,
    onClick: (Int) -> Unit
) {
    Button(
        onClick = { onClick(number) },
        modifier = Modifier.size(64.dp)
    ) {
        Text(number.toString(), style = MaterialTheme.typography.labelLarge)
    }
}

@Preview
@Composable
private fun PreviewNumberPad() {
    NumberPad(
        onNumberClick = { },
        onRemoveClick = { },
        onStartClick = { },
        onStopClick = { },
        isActive = true
    )
}