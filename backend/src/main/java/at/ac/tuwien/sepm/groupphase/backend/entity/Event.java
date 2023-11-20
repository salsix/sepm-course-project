package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Min(1)
    private int duration; //in minutes
    @Lob
    @NotBlank
    private String description;
    @Lob
    @NotBlank
    private String artists;
    private boolean image;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private EventCategory category;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "event")
    private Set<Show> shows;

    public Event() {
    }

    public Event(Long id, String name, int duration, String description, String artists, EventCategory category, Set<Show> shows) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.artists = artists;
        this.category = category;
        this.shows = shows;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Set<Show> getShows() {
        return shows;
    }

    public void setShows(Set<Show> shows) {
        this.shows = shows;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return getDuration() == event.getDuration()
            && isImage() == event.isImage()
            && Objects.equals(getId(), event.getId())
            && Objects.equals(getName(), event.getName())
            && Objects.equals(getDescription(), event.getDescription())
            && Objects.equals(getArtists(), event.getArtists())
            && Objects.equals(getCategory(), event.getCategory())
            && Objects.equals(getShows(), event.getShows());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDuration(), getDescription(), getArtists(), isImage(), getCategory(), getShows());
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", duration=" + duration +
            ", description='" + description + '\'' +
            ", artists='" + artists + '\'' +
            ", image=" + image +
            ", category=" + category +
            ", shows=" + shows +
            '}';
    }
}
