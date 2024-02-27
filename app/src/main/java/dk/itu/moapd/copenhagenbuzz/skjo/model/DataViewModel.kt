package dk.itu.moapd.copenhagenbuzz.skjo.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DataViewModel : ViewModel() {

    // The internal MutableLiveData that stores the list of events
    private val _events = MutableLiveData<List<Event>>()

    // The external LiveData interface to the list of events
    val events: LiveData<List<Event>> = _events

    init {
        fetchEvents()
    }

    // Asynchronously fetches the list of events
    private fun fetchEvents() {
        viewModelScope.launch(Dispatchers.IO) {

            //change this to faker or just do the database
            val testData = listOf(
                Event("Cph Festival", "Copenhagen Downtown", "Fri, Feb 02 2024 - Sun, Feb 25 2024", "Festival", "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium."),
                Event("Event 2", "Location 2", "Date 2", "Type 2", "Description 2"),
                Event("Event 3", "Location 3", "Date 3", "Type 3", "Description 3"),
                Event("Event 4", "Location 4", "Date 4", "Type 4", "Description 4"),
                Event("Event 5", "Location 5", "Date 5", "Type 5", "Description 5")
            )

            // Update LiveData with the fetched events
            _events.postValue(testData)
        }
    }
}