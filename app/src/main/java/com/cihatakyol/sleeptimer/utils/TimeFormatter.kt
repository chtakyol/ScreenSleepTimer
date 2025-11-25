package com.cihatakyol.sleeptimer.utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimeFormatter @Inject constructor() {
    fun formatTime(millis: Long): String {
        val hours = millis / (1000 * 60 * 60)
        val minutes = (millis % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = (millis % (1000 * 60)) / 1000
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun formatTimeShort(millis: Long): String {
        val minutes = millis / (1000 * 60)
        val seconds = (millis % (1000 * 60)) / 1000
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun parseTimeToMillis(timeString: String): Long {
        return try {
            val parts = timeString.split(":")
            when (parts.size) {
                2 -> { // MM:SS format
                    val minutes = parts[0].toInt()
                    val seconds = parts[1].toInt()
                    ((minutes * 60 + seconds) * 1000).toLong()
                }
                3 -> { // HH:MM:SS format
                    val hours = parts[0].toInt()
                    val minutes = parts[1].toInt()
                    val seconds = parts[2].toInt()
                    ((hours * 3600 + minutes * 60 + seconds) * 1000).toLong()
                }
                else -> 0L
            }
        } catch (e: Exception) {
            0L
        }
    }

    fun formatToMillis(time: Pair<Int, Int>): Long {
        val (hours, minutes) = time
        return (hours * 60L * 60L * 1000L) + (minutes * 60L * 1000L)
    }

    fun parseFromMillis(millis: Long): Pair<Int, Int> {
        val hours = (millis / (1000 * 60 * 60)).toInt()
        val minutes = ((millis % (1000 * 60 * 60)) / (1000 * 60)).toInt()
        return Pair(hours, minutes)
    }
}