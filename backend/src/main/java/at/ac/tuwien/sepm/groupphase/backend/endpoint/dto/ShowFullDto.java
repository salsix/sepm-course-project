package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ShowFullDto {
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
    private HallplanDto hallplan;

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

    public HallplanDto getHallplan() {
        return hallplan;
    }

    public void setHallplan(HallplanDto hallplan) {
        this.hallplan = hallplan;
    }

    @Override
    public String toString() {
        return "ShowFullDto{" +
            "id=" + id +
            ", date=" + date +
            ", hour=" + hour +
            ", minute=" + minute +
            ", event=" + event +
            ", hallplan=" + hallplan +
            '}';
    }
}
