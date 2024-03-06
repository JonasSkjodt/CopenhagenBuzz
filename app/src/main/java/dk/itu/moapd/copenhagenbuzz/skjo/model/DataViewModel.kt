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

/**
 * the ViewModel holds LiveData, keeping data separate from the UI logic
 */

class DataViewModel : ViewModel() {

    //loader showing for slow faker data
    val isLoading = MutableLiveData<Boolean>()

    // timeline listview
    // The internal MutableLiveData that stores the list of events
    private val _events = MutableLiveData<List<Event>>()

    /**
     * The external LiveData interface to the list of events
     * @see [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
     */
    val events: LiveData<List<Event>> = _events

    //favorites recycleview
    private val _favorites = MutableLiveData<List<Event>>()
    var favorites: LiveData<List<Event>> = _favorites

    /**
     * Initializes the ViewModel by fetching the initial lists of events and favorites.
     */
    init {
        //events data
        fetchEvents()
        //favorites data
        fetchFavorites()
    }

    /**
     *  Fetches a list of fake events asynchronously using coroutines and updates the LiveData.
     *  Uses the Faker library to generate a list of events with randomized data.
     *  The loading status is updated before and after the data fetching process, displayed "loading faker data" (from xml)
     * @see [Coroutines](https://developer.android.com/topic/libraries/architecture/coroutines)
     */
    private fun fetchEvents() {
        // Start loader while faker is fetching data
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

        // Stop loader when faker shows its data
        isLoading.postValue(false)
        }
    }
    /**
    * Initializes the favorites LiveData with an empty list.
    * It's initialized in init() to set up the initial state of the favorites list.
    */
    private fun fetchFavorites() {
        //should be empty in the start
        _favorites.value = listOf()
    }
    /**
     * Adds a new event to the favorites list if it's not already contained within it.
     * The method updates the LiveData with a new list that includes the added event.
     * (Currently logs are provided for debug purposes)
     *
     * @param event The event to be added to the favorites list.
     */
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
    /**
     * Removes an event from the favorites list.
     * The method updates the LiveData with a new list that excludes the removed event.
     *
     * @param event The event to be removed from the favorites list.
     */
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