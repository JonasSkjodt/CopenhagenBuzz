package dk.itu.moapd.copenhagenbuzz.skjo.model

/**
 * The Event data class represents various details of a user input event
 *
 * @property eventName the name of the event.
 * @property eventLocation the location of the event.
 * @property eventDate the event range of date.
 * @property eventType the type of event.
 * @property eventDescription the description of the event.
 *
 * @see [Kotlin Documentation on Data Classes](https://kotlinlang.org/docs/data-classes.html)
 */

data class Event(
    var eventName: String,
    var eventLocation: String,
    var eventDate: String, //date
    var eventType: String, //eventType
    var eventDescription: String
)