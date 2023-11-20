package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.EventCategory;

import java.util.Objects;

public class EventSimpleDto {
    private Long id;
    private String name;
    private int duration; //in minutes
    private String description;
    private String artists;
    private boolean image;
    private EventCategory category;

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
        EventSimpleDto that = (EventSimpleDto) o;
        return getDuration() == that.getDuration()
            && isImage() == that.isImage()
            && Objects.equals(getId(), that.getId())
            && Objects.equals(getName(), that.getName())
            && Objects.equals(getDescription(), that.getDescription())
            && Objects.equals(getArtists(), that.getArtists())
            && Objects.equals(getCategory(), that.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDuration(), getDescription(), getArtists(), isImage(), getCategory());
    }

    @Override
    public String toString() {
        return "EventSimpleDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", duration=" + duration +
            ", description='" + description + '\'' +
            ", artists='" + artists + '\'' +
            ", image=" + image +
            ", eventCategory=" + category +
            '}';
    }
}
