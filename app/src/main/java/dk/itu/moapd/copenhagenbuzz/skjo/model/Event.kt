package dk.itu.moapd.copenhagenbuzz.skjo.model

/**
 * The Event data class represents various details of a user generated event
 *
 * @param eventName the name of the event.
 * @param eventLocation the location of the event.
 * @param eventDate the event range of date.
 * @param eventType the type of event.
 * @param eventDescription the description of the event.
 *
 * @see [Kotlin Documentation on Data Classes](https://kotlinlang.org/docs/data-classes.html)
 */

data class Event(
    var eventName: String,
    var eventLocation: String,
    var eventDate: String,
    var eventType: String,
    var eventDescription: String
)