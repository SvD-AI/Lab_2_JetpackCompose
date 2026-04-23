package com.example.lab_2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab_2.model.Habit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitViewModel : ViewModel() {

    private val initialHabits = listOf(
        Habit(1, "Ранкова зарядка"),
        Habit(2, "Читання книги (20 хв)"),
        Habit(3, "Випити 2л води"),
        Habit(4, "Вивчення нових слів"),
        Habit(5, "Медитація"),
        Habit(6, "Прогулянка на свіжому повітрі")
    )

    private val _habits = MutableStateFlow(initialHabits)
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    val doneCount: StateFlow<Int> = _habits
        .map { list -> list.count { it.isDone } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    val totalCount: Int = initialHabits.size

    fun toggleHabit(habitId: Int) {
        _habits.value = _habits.value.map {
            if (it.id == habitId) it.copy(isDone = !it.isDone) else it
        }
    }

    fun resetDay() {
        _habits.value = _habits.value.map { it.copy(isDone = false) }
    }
}
