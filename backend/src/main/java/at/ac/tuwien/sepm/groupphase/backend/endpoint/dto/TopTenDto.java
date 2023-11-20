package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class TopTenDto {
    private EventSimpleDto event;
    private long count;

    public TopTenDto() {
    }

    public TopTenDto(EventSimpleDto event, long count) {
        this.event = event;
        this.count = count;
    }

    public EventSimpleDto getEvent() {
        return event;
    }

    public void setEvent(EventSimpleDto event) {
        this.event = event;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TopTenDto{" +
            "event=" + event +
            ", count=" + count +
            '}';
    }
}
