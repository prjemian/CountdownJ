package example.callbacks;

import example.callbacks.model.EventNotifier;
import example.callbacks.model.InterestingEvent;

public class CallMe implements InterestingEvent {
	private EventNotifier en;

	public CallMe() {
		// Create the event notifier and pass ourself to it.
		en = new EventNotifier(this);
	}

	// Define the actual handler for the event.
	public void interestingEvent() {
		// Wow! Something really interesting must have occurred!
		// Do something...
	}
	// ...
}
