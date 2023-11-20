package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.EventCategory;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

public class EventDto {
    private Long id;
    @NotBlank
    private String name;
    @NotNull @Min(1)
    private int duration; //in minutes
    @NotBlank
    private String description;
    @NotBlank
    private String artists;
    @NotNull
    private EventCategory category;
    private boolean image;
    private Set<ShowDto> shows;

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

    public boolean getImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public Set<ShowDto> getShows() {
        return shows;
    }

    public void setShows(Set<ShowDto> shows) {
        this.shows = shows;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public boolean isImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDto eventDto = (EventDto) o;
        return getDuration() == eventDto.getDuration()
            && isImage() == eventDto.isImage()
            && Objects.equals(getId(), eventDto.getId())
            && Objects.equals(getName(), eventDto.getName())
            && Objects.equals(getDescription(), eventDto.getDescription())
            && Objects.equals(getArtists(), eventDto.getArtists())
            && Objects.equals(getCategory(), eventDto.getCategory())
            && Objects.equals(getShows(), eventDto.getShows());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDuration(), getDescription(), getArtists(), getCategory(), isImage(), getShows());
    }

    @Override
    public String toString() {
        return "EventDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", duration=" + duration +
            ", description='" + description + '\'' +
            ", artists='" + artists + '\'' +
            ", eventCategory=" + category +
            ", image=" + image +
            ", shows=" + shows +
            '}';
    }
}
