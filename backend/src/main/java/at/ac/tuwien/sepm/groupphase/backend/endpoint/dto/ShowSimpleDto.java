package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class ShowSimpleDto {
    private Long id;
    @NotNull
    private LocalDate date;
    @NotNull
    @Min(0)
    @Max(23)
    private Integer hour;
    @NotNull
    @Min(0)
    @Max(59)
    private Integer minute;
    private EventSimpleDto event;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public EventSimpleDto getEvent() {
        return event;
    }

    public void setEvent(EventSimpleDto event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "ShowSimpleDto{" +
            "id=" + id +
            ", date=" + date +
            ", hour=" + hour +
            ", minute=" + minute +
            ", event=" + event +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowSimpleDto that = (ShowSimpleDto) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(hour, that.hour) &&
            Objects.equals(minute, that.minute) &&
            Objects.equals(event, that.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, hour, minute, event);
    }
}
