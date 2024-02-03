package dk.itu.moapd.copenhagenbuzz.skjo.model

//data classes doesn't need to add custom logic
//to getters and setters like in java, so the below is enough
//https://kotlinlang.org/docs/data-classes.html
data class Event(
    var eventName: String,
    var eventLocation: String,
    var eventDate: String,
    var eventType: String,
    var eventDescription: String
)