package dk.itu.moapd.copenhagenbuzz.skjo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FavoriteRowItemBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.Event

/**
 * FavoriteAdapter uses a RecyclerView that presents a list of favorite events in the favorites fragment.
 *
 * @property events The list of favorite events to be displayed.
 */
class FavoritesAdapter(private var events: List<Event>) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavoriteRowItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }
    /**
     * Updates the list of favorite events that the adapter is displaying and
     * notifies the RecyclerView that the data set has changed, making it refresh the UI.
     *
     * @param newEvents The new list of favorite events to display.
     */
    fun updateEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
    /**
     * The ViewHolder class is for the favorite event items within the RecyclerView.
     *
     * @property binding The binding for the favorite row item view.
     */
    class ViewHolder(private val binding: FavoriteRowItemBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds an event to the view holder, setting the text and image from the event
         * on the respective views in the layout.
         *
         * @param event The event to be bound to the view holder.
         */
        fun bind(event: Event) {
            with(binding) {
                cardTimelineTextEventName.text = event.eventName
                cardTimelineTextEventType.text = event.eventType
                Picasso.get()
                    .load(event.eventImage) // load the URL to the image
                    .into(cardTimelineTextEventImage) // and then insert it into the xml imageview
            }
        }
    }
}