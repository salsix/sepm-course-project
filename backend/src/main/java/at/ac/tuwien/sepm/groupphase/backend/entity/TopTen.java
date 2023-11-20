package at.ac.tuwien.sepm.groupphase.backend.entity;

public class TopTen {
    private Event event;
    private long count;

    public TopTen() {
    }

    public TopTen(Event event, long count) {
        this.event = event;
        this.count = count;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }


}
