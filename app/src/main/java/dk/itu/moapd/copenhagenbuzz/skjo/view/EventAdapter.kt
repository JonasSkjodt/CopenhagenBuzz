package dk.itu.moapd.copenhagenbuzz.skjo.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.EventRowItemBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.Event

class EventAdapter(private var events: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EventRowItemBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun updateEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }

    class EventViewHolder(private val binding: EventRowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            with(binding) {
                textEventTitle.text = event.eventName
                textEventLocation.text = event.eventLocation
                textEventDate.text = event.eventDate
                // Additional binding for other views can be added here

                // Example: If button_edit_event should be visible for the event owner
                // buttonEditEvent.visibility = if (event.isOwner) View.VISIBLE else View.GONE
            }
        }
    }
}