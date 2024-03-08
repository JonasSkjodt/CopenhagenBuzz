package dk.itu.moapd.copenhagenbuzz.skjo.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.skjo.databinding.EventRowItemBinding
import dk.itu.moapd.copenhagenbuzz.skjo.model.DataViewModel
import dk.itu.moapd.copenhagenbuzz.skjo.model.Event

/**
 * EventAdapter is used for the ListView that presents Event data in the TimeLine UI.
 *
 * @property events The list of events to be displayed.
 * @property context The context in which the adapter is operating.
 * @property viewModel The ViewModel that provides event data and business logic.
 */
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
            //use picasso implementation to use an image url and insert it into the imageview
            Picasso.get()
                .load(event.eventImage) // load the URL to the image
                .into(cardTimelineTextEventImage) // and then insert it into the xml imageview
            //card favorite checkbox
            cardFavoriteIcon(binding,event)
            //card button listeners
            editButtonListener(binding)
            infoButtonListener(binding,event)
        }
        return binding.root
    }

    /**
     * Configures the favorite icon for each event in the ListView.
     * Sets the checked state based on whether the event is in the favorites list and
     * attaches listeners to handle changes in the favorite state.
     *
     * @param binding The binding for the event row item view.
     * @param event The event associated with the current row being processed.
     */
    private fun cardFavoriteIcon(binding: EventRowItemBinding, event: Event) {
        // Detach any existing listeners to avoid unwanted behavior
        // If the setOnCheckedChangeListener is not set to null first, a bug shows where other events are favorited (without having been set to favorite by the user)
        // This is because when scrolling through the listview, the favorited items gets recycled to display a new item
        // this means, if the setOnCheckedChangeListener is not set to null initially before reuse
        // the recycled view could have the old listener attached
        // so make sure the checkbox is only tied to the specific data of the item being displayed each time an event is loaded
        binding.cardFavoriteIcon.setOnCheckedChangeListener(null)

        //favorite heart checkbox on the materialcardview
        binding.cardFavoriteIcon.isChecked = viewModel.isFavorite(event)

        // Define what happens when the favorite icon's checked state changes
        binding.cardFavoriteIcon.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addFavorite(event)
                Toast.makeText(binding.root.context, "Added to favorites", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.removeFavorite(event)
                Toast.makeText(binding.root.context, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Attaches a listener to the edit button of each event in the ListView.
     * Currently displays a placeholder message when the edit button is clicked.
     *
     * @param binding The binding for the event row item view.
     *
     * @see [Dialogs](https://developer.android.com/develop/ui/views/components/dialogs)
     */
    private fun editButtonListener(binding: EventRowItemBinding) {
        binding.cardTimelineButtonEdit.setOnClickListener { view ->
            val builder = AlertDialog.Builder(view.context)
            builder
                .setMessage("This button will be modified later")
                .setPositiveButton("OK") { dialog, id ->
                    // something happens maybe
                }
            builder.create().show()
        }
    }

    /**
     * Attaches a listener to the info button of each event in the ListView.
     * Displays the full event description in a dialog when the info button is clicked.
     *
     * @param binding The binding for the event row item view.
     * @param event The event associated with the current row being processed.
     */
    private fun infoButtonListener(binding: EventRowItemBinding, event: Event) {
        binding.cardTimelineButtonInfo.setOnClickListener { view ->
            val builder = AlertDialog.Builder(view.context)
            builder
                .setMessage(event.eventDescription)
                .setTitle(event.eventName)
                .setPositiveButton("OK") { dialog, id ->
                    // something happens maybe
                }
            builder.create().show()
        }
    }
    /**
     * Updates the list of events that the adapter is displaying and notifies the
     * ListView that the data set has changed, making it refresh the UI.
     *
     * @param newEvents The new list of events to display.
     */
    fun updateEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}