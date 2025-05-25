package com.example.app3fragment

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.app3fragment.database.DataBaseManager
import com.example.app3fragment.server.Server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class App3 : Application() {
    private lateinit var database: DataBaseManager;

    override fun onCreate() {
        super.onCreate()
        val scope = CoroutineScope(Dispatchers.IO)
        this.database = Room.databaseBuilder(this.applicationContext, DataBaseManager::class.java, "app_db5.db").build()
        Server.KtorServerManager.start(scope, this.database.labelDao(), this.database.companyDao(), this.database.programDao())

        val context: Context = this.applicationContext
        val dbFile = context.getDatabasePath("app_db5.db")
        Log.d("Database Path", dbFile.absolutePath)
    }

    override fun onTerminate() {
        super.onTerminate()
        Server.KtorServerManager.stop()
    }
}