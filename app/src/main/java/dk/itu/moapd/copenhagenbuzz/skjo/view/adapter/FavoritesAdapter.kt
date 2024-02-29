package dk.itu.moapd.copenhagenbuzz.skjo.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.FavoriteRowItemBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.Event

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

    fun updateEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: FavoriteRowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            with(binding) {
                cardTimelineTextEventName.text = event.eventName
                //cardTimelineTextEventLocation.text = event.eventLocation
                cardTimelineTextEventType.text = event.eventType
                //cardTimelineTextEventDate.text = event.eventDate
                //cardTimelineTextEventDescription.text = event.eventDescription
            }
        }
    }
}