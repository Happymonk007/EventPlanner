package com.example.eventplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.eventplanner.ui.AppRoot
import com.example.eventplanner.ui.theme.EventPlannerTheme
import com.example.eventplanner.work.WorkScheduler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WorkScheduler.scheduleEventsRefresh(this)
        setContent {
            EventPlannerTheme {
                AppRoot()
            }
        }
    }
}