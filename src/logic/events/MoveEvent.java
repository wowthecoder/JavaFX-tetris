package logic.events;

public class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;
	public MoveEvent(EventType eventType, EventSource eventSource) {
		this.eventType = eventType;
		this.eventSource = eventSource;
	}
	public EventType getEventType() {
		return eventType;
	}
	public EventSource getEventSource() {
		return eventSource;
	}
    
}
