package dk.itu.moapd.copenhagenbuzz.skjo.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.EventRowItemBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.DataViewModel
import dk.itu.moapd.copenhagenbuzz.skjo.model.Event

class EventAdapter(
    private var events: List<Event>,
    private val context: Context,
    private val viewModel: DataViewModel
) : BaseAdapter() {

    override fun getCount(): Int = events.size

    override fun getItem(position: Int): Any = events[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: EventRowItemBinding
        if (convertView == null) {
            binding = EventRowItemBinding.inflate(LayoutInflater.from(context), parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as EventRowItemBinding
        }

        val event = getItem(position) as Event

        with(binding) {
            cardTimelineTextEventName.text = event.eventName
            cardTimelineTextEventLocation.text = event.eventLocation
            cardTimelineTextEventType.text = event.eventType
            cardTimelineTextEventDate.text = event.eventDate
            cardTimelineTextEventDescription.text = event.eventDescription
            //cardTimelineTextEventImage.text = event.eventImage

            // Detach any existing listeners to avoid unwanted behavior
            // If the setOnCheckedChangeListener is not set to null first, a bug displays when scrolling through the listview because the favorited items gets recycled to display a new item
            //i.e, if the setOnCheckedChangeListener is not set to null initially before reuse
            //tje recycled view could have the old listener attached
            //so we make sure the checkbox is only tied to the specific data of the item being displayed
            cardFavoriteIcon.setOnCheckedChangeListener(null)

            //favorite heart checkbox on materialcardview
            cardFavoriteIcon.isChecked = viewModel.isFavorite(event)
            cardFavoriteIcon.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.addFavorite(event)
                    Toast.makeText(binding.root.context, "Added to favorites", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.removeFavorite(event)
                    Toast.makeText(binding.root.context, "Removed from favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
    }

    fun updateEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}