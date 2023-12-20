package com.bbva.utilitieslib.utils

import java.util.Calendar

class TimeSpan(private val ticks: Long = 0): Comparable<TimeSpan> {
    val days: Int
        get() = (ticks / millisPerDay).toInt()

    val hours: Int
        get() =  ((ticks / millisPerHour) % hoursPerDay).toInt()

    val milliSeconds: Int
        get() = (ticks % millisPerSecond).toInt()

    val minutes: Int
        get() = ((ticks / millisPerMinute) % minutePerHour).toInt()

    val seconds: Int
        get() = ((ticks / millisPerSecond) % secondPerMinute).toInt()

    val totalDay: Double
        get() = ticks.toDouble() * dayPerMillis

    val totalHours: Double
        get() = ticks.toDouble() * hourPerMillis

    val totalMilliseconds: Long
        get() =  ticks

    val totalMinutes: Double
        get() = ticks.toDouble() * minutePerMillis

    val totalSeconds: Double
        get() = ticks.toDouble() * secondPerMillis

    constructor(calendar: Calendar) : this(calendar.timeInMillis)
    constructor(hours: Int, minutes: Int, seconds: Int): this(timeToTicks(hours, minutes, seconds))

    constructor(days: Int, hours: Int, minutes: Int, seconds: Int, milliseconds: Int = 0) :
            this(dateToTicks(days, hours, minutes, seconds, milliseconds))
    override fun compareTo(other: TimeSpan): Int {
        if (ticks > other.ticks) return 1
        if (ticks < other.ticks) return -1
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if(super.equals(other))
            return true

        if (javaClass != other?.javaClass)
            return false

        return (ticks == (other as TimeSpan).ticks)
    }

    override fun hashCode() = ticks.toInt() xor(ticks shr 32).toInt()

    operator fun plus(value: TimeSpan): TimeSpan{
        val result: Long = ticks + value.ticks

        if ((ticks shr 63 == value.ticks shr 63) && (ticks shr 63 != result shr 63))
            throw IndexOutOfBoundsException("Overflow_TimeSpanTooLong")

        return TimeSpan(result)
    }

    operator fun minus(value: TimeSpan): TimeSpan{
        val result = ticks - value.ticks

        if ((ticks shr 63 == value.ticks shr 63) && (ticks shr 63 != result shr 63))
            throw IndexOutOfBoundsException("Overflow_TimeSpanTooLong")

        return TimeSpan(result)
    }

    fun toCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = ticks
        return calendar
    }

    companion object{
        private const val secondPerMinute: Int = 60
        private const val minutePerHour: Int = 60

        private const val hoursPerDay: Int = 24
        private const val secondsPerHour: Int = secondPerMinute * minutePerHour

        private const val millisPerSecond: Int = 1000
        private const val secondPerMillis: Double = 1.0 / millisPerSecond

        private const val millisPerMinute: Int = millisPerSecond * secondPerMinute
        private const val minutePerMillis: Double = 1.0 / millisPerMinute

        private const val millisPerHour: Int = millisPerMinute * minutePerHour
        private const val hourPerMillis: Double = 1.0 / millisPerHour

        private const val millisPerDay: Int = millisPerHour * hoursPerDay
        private const val dayPerMillis: Double = 1.0 / millisPerDay

        private const val maxSeconds: Long = Long.MAX_VALUE / millisPerSecond
        private const val minSeconds: Long = Long.MIN_VALUE / millisPerSecond

        private const val maxMillisSeconds: Long = Long.MAX_VALUE
        private const val minMillisSeconds: Long = Long.MIN_VALUE

        val ZERO = TimeSpan(0)
        val MAX_VALUE: TimeSpan = TimeSpan(Long.MAX_VALUE)
        val MIN_VALUE: TimeSpan = TimeSpan(Long.MIN_VALUE)

        private fun getTotalSeconds(hour: Int, minute: Int, seconds: Int) =
            hour.toLong() * secondsPerHour + minute.toLong() * secondPerMinute + seconds

        private fun dateToTicks(days: Int, hours: Int, minutes: Int, seconds: Int, milliseconds: Int): Long{
            val totalMilliSeconds: Long = (days.toLong() * secondsPerHour * hoursPerDay +
                    getTotalSeconds(hours, minutes, seconds)) * millisPerSecond + milliseconds
            if (totalMilliSeconds > maxMillisSeconds || totalMilliSeconds < minMillisSeconds)
                throw IndexOutOfBoundsException("Over Flow TimeSpan Too Long")

            return totalMilliSeconds
        }

        fun timeToTicks(hours: Int, minutes: Int, seconds: Int): Long{
            val totalSeconds = getTotalSeconds(hours, minutes, seconds)
            if (totalSeconds > maxSeconds || totalSeconds < minSeconds)
                throw IndexOutOfBoundsException("Over Flow TimeSpan Too Long")

            return totalSeconds * millisPerSecond
        }


    }
}