package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public abstract class HallplanData {
    @Id
    @GeneratedValue
    private Long id;
    private Integer count;
    private String identifier;
    private Integer rowNumber;
    private Integer cat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getCat() {
        return cat;
    }

    public void setCat(Integer cat) {
        this.cat = cat;
    }

    public HallplanData() {
    }

    public HallplanData(Long id, Integer count, String identifier, Integer rowNumber,
                        Integer cat) {
        this.id = id;
        this.count = count;
        this.identifier = identifier;
        this.rowNumber = rowNumber;
        this.cat = cat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HallplanData that = (HallplanData) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(count, that.count) &&
            Objects.equals(identifier, that.identifier) &&
            Objects.equals(rowNumber, that.rowNumber) &&
            Objects.equals(cat, that.cat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count, identifier, rowNumber, cat);
    }

    @Override
    public String toString() {
        return "HallplanData{" +
            "id=" + id +
            ", count=" + count +
            ", identifier='" + identifier + '\'' +
            ", rowNumber=" + rowNumber +
            ", cat=" + cat +
            '}';
    }
}
