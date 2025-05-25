package com.example.app3fragment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app3fragment.database.artist.Artist
import com.example.app3fragment.database.artist.ArtistDAO
import com.example.app3fragment.database.fan.Fan
import com.example.app3fragment.database.fan.FanDAO
import com.example.app3fragment.database.label.Label
import com.example.app3fragment.database.label.LabelDAO

@Database(entities = [Label::class, Artist::class, Fan::class], version = 4, exportSchema = false)
abstract class DataBaseManager : RoomDatabase() {
    abstract fun labelDao(): LabelDAO
    abstract fun companyDao(): ArtistDAO
    abstract fun programDao(): FanDAO
}