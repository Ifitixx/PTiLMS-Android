package com.pizzy.ptilms.data.local

import androidx.room.TypeConverter
import com.pizzy.ptilms.data.model.CommunicationType

class Converters {
    @TypeConverter
    fun fromCommunicationType(value: CommunicationType): String {
        return value.name
    }

    @TypeConverter
    fun toCommunicationType(value: String): CommunicationType {
        return CommunicationType.valueOf(value)
    }
}