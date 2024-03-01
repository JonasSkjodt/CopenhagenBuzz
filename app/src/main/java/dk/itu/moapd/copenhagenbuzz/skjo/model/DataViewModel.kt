package dk.itu.moapd.copenhagenbuzz.skjo.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.javafaker.Faker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class DataViewModel : ViewModel() {

    //loader showing for slow faker data
    val isLoading = MutableLiveData<Boolean>()

    //timeline listview
    // The internal MutableLiveData that stores the list of events
    private val _events = MutableLiveData<List<Event>>()
    // The external LiveData interface to the list of events
    // @see https://developer.android.com/topic/libraries/architecture/livedata
    val events: LiveData<List<Event>> = _events

    //favorites recycleview
    private val _favorites = MutableLiveData<List<Event>>()
    var favorites: LiveData<List<Event>> = _favorites

    init {
        //events data
        fetchEvents()
        //favorites data
        fetchFavorites()
    }

    /**
     * Asynchronously fetches the list of events (coroutines)
     * @see [Coroutines](https://developer.android.com/topic/libraries/architecture/coroutines)
     */
    private fun fetchEvents() {
        // Start loading
        isLoading.postValue(true)
        // Initialize the Faker instance with a fixed random seed for reproducibility
        // @see https://github.com/fabricionarcizo/moapd2024/blob/main/lecture05/05-1_RecyclerView/app/src/main/java/dk/itu/moapd/recyclerview/MainFragment.kt
        val faker = Faker()
        viewModelScope.launch(Dispatchers.IO) {
            val fakeEvents = List(50) {
                Event(
                    eventName = faker.rockBand().name(),
                    eventLocation = "${faker.address().cityName()}, ${faker.address().country()}",
                    eventDate = faker.date().future(365, TimeUnit.DAYS).toString(),
                    eventType = faker.book().genre(),
                    eventDescription = faker.lorem().paragraph(),
                    eventImage = "https://picsum.photos/seed/$it/400/194"
                )
            }
        // Data has been fetched, post to LiveData
        _events.postValue(fakeEvents)

        // Stop loading when faker shows its data
        isLoading.postValue(false)
        }
    }
    //favorites
    private fun fetchFavorites() {
        //should be empty in the start
        _favorites.value = listOf()
    }

    fun addFavorite(event: Event) {
        // Log statement to indicate the method is called
        Log.d("DataViewModel", "Add favorite event: ${event.eventName}")

        val currentFavorites = _favorites.value ?: listOf()
        if (!currentFavorites.contains(event)) {
            val updatedFavorites = (currentFavorites + event)
            _favorites.postValue(updatedFavorites)

            // Log statement to show the list after adding an event
            Log.d("DataViewModel", "Updated favorites: ${updatedFavorites.joinToString { it.eventName }}")
        }
    }

    fun removeFavorite(event: Event) {
        //remove an event from the favorites list
        val currentFavorites = _favorites.value ?: listOf()
        _favorites.postValue(currentFavorites - event)
    }

    /**
     * Checks if the given event is marked as a favorite.
     *
     * This function examines the list of favorited events and determines whether
     * the provided event is contained within that list.
     *
     * @param event The event to check for favorite status.
     * @return True if the event is currently marked as a favorite, false otherwise.
     */
    fun isFavorite(event: Event): Boolean {
        return _favorites.value?.contains(event) ?: false
    }
}