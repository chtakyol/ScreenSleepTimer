package com.cihatakyol.sleeptimer.ui.screens.mainscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    isTimerActive: Boolean,
    currentHour: Int,
    currentMinute: Int,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
) {
    val hours = (0..23).toList()
    val minutes = (0..59).toList()

    Row(
        modifier = modifier.height(150.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SwipeSelector(
            isTimerActive = isTimerActive,
            values = hours,
            selectedIndex = hours.indexOf(currentHour),
            onValueSelected = { hour -> onTimeSelected(hour, currentMinute) }
        )

        Text("h", color = MaterialTheme.colorScheme.onSurface)

        Spacer(modifier = Modifier.width(16.dp))

        SwipeSelector(
            isTimerActive = isTimerActive,
            values = minutes,
            selectedIndex = minutes.indexOf(currentMinute),
            onValueSelected = { minute -> onTimeSelected(currentHour, minute) }
        )

        Text("m", color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun SwipeSelector(
    isTimerActive: Boolean,
    values: List<Int>,
    selectedIndex: Int,
    onValueSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(selectedIndex) {
        if (!listState.isScrollInProgress) { // 👈 don't fight the user
            coroutineScope.launch {
                listState.animateScrollToItem(selectedIndex)
            }
        }
    }
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val visibleIndex = listState.firstVisibleItemIndex
            val scrollOffset = listState.firstVisibleItemScrollOffset
            val centerIndex = visibleIndex + if (scrollOffset > 50) 1 else 0
            val clampedIndex = centerIndex.coerceIn(0, values.lastIndex)

            if (clampedIndex != selectedIndex) {
                if (!isTimerActive) {
                    onValueSelected(values[clampedIndex]) // update external state
                } else return@LaunchedEffect
            }
        }
    }
    Box(modifier = modifier.width(60.dp)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = 50.dp),
            userScrollEnabled = !isTimerActive
        ) {
            items(values.size) { index ->
                val isSelected = index == selectedIndex
                Text(
                    text = values[index].toString().padStart(2, '0'),
                    fontSize = if (isSelected) 28.sp else 18.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .height(48.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSwipeTimePicker() {
    TimePicker(
        isTimerActive = false,
        currentHour = 12,
        currentMinute = 15,
        onTimeSelected = { hour, minute -> }
    )
}
