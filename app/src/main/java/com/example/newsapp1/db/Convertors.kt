package com.example.newsapp1.db

import androidx.room.TypeConverter
import com.example.newsapp1.models.Source

class Convertors {

    @TypeConverter
    fun fromSource(source: Source) : String{
        return source.name.toString()
    }
    @TypeConverter
    fun toSource(name: String): Source{
        return Source(name,name)
    }

}