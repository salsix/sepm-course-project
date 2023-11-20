package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Show {
    @Id
    @GeneratedValue
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hallplan hallplan;

    public Show() {
    }

    public Show(Long id, @NotNull @Future LocalDate date, @NotNull @Min(0) @Max(23) Integer hour, @NotNull @Min(0) @Max(59) Integer minute, Event event, Hallplan hallplan) {
        this.id = id;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.event = event;
        this.hallplan = hallplan;
    }

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

    public Event getEvent() {
        return event;
    }

    public Hallplan getHallplan() {
        return hallplan;
    }

    public void setHallplan(Hallplan hallplan) {
        this.hallplan = hallplan;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "Show{" +
            "id=" + id +
            ", date=" + date +
            ", hour=" + hour +
            ", minute=" + minute +
            ", event=" + event +
            ", hallplan=" + hallplan +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return Objects.equals(id, show.id) &&
            Objects.equals(date, show.date) &&
            Objects.equals(hour, show.hour) &&
            Objects.equals(minute, show.minute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, hour, minute);
    }
}
