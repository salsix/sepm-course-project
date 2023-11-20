package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class News {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String shortDescription;
    @NotBlank @Lob
    private String content;
    private LocalDateTime publishedAt;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ApplicationUser> wasReadByUsers;
    private int imageCount;

    public News(Long id, String title, String shortDescription, String content,
        List<ApplicationUser> wasReadByUsers,
        Integer imageCount) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.content = content;
        this.wasReadByUsers = wasReadByUsers;
        this.imageCount = imageCount;
    }

    public News() {
    }

    public List<ApplicationUser> getWasReadByUsers() {
        return wasReadByUsers;
    }

    public void setWasReadByUsers(List<ApplicationUser> wasReadByUsers) {
        this.wasReadByUsers = wasReadByUsers;
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

    @Override
    public String toString() {
        return "News{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", shortDescription='" + shortDescription + '\'' +
            ", content='" + content + '\'' +
            ", publishedAt=" + publishedAt +
            ", wasReadByUsers=" + wasReadByUsers +
            ", imageCount=" + imageCount +
            '}';
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int image) {
        this.imageCount = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof News)) {
            return false;
        }
        News news = (News) o;
        return imageCount == news.imageCount && Objects.equals(id, news.id) && Objects
            .equals(title, news.title) && Objects
            .equals(shortDescription, news.shortDescription) && Objects
            .equals(content, news.content) && Objects.equals(publishedAt, news.publishedAt)
            && Objects.equals(wasReadByUsers, news.wasReadByUsers);
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(id, title, shortDescription, content, publishedAt, wasReadByUsers, imageCount);
    }
}
