package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewsDto {

    private Long id;

    @NotBlank
    private String title;
    @NotBlank
    @Size(max = 150)
    private String shortDescription;
    @NotBlank
    @Size(max = 1000)
    private String content;
    private LocalDateTime publishedAt;
    private Boolean alreadyRead;
    private int imageCount;

    public NewsDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Boolean getAlreadyRead() {
        return alreadyRead;
    }

    public void setAlreadyRead(Boolean alreadyRead) {
        this.alreadyRead = alreadyRead;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    @Override
    public String toString() {
        return "NewsDto{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", shortDescription='" + shortDescription + '\'' +
            ", content='" + content + '\'' +
            ", publishedAt=" + publishedAt +
            ", alreadyRead=" + alreadyRead +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewsDto newsDto = (NewsDto) o;
        return Objects.equals(id, newsDto.id) && Objects.equals(title, newsDto.title)
            && Objects.equals(shortDescription, newsDto.shortDescription) && Objects
            .equals(content, newsDto.content)
            && Objects.equals(publishedAt, newsDto.publishedAt) && Objects
            .equals(alreadyRead, newsDto.alreadyRead);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, shortDescription, content, publishedAt, alreadyRead);
    }
}
