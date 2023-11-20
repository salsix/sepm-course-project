package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

public abstract class HallplanDataDto {

    private Integer count;
    private String identifier;
    private Integer rowNumber;
    private Integer cat;

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

    @Override
    public String toString() {
        return "HallplanDataDto{" +
            "count=" + count +
            ", identifier='" + identifier + '\'' +
            ", rowNumber=" + rowNumber +
            ", cat=" + cat +
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
        HallplanDataDto that = (HallplanDataDto) o;
        return Objects.equals(count, that.count) &&
            Objects.equals(identifier, that.identifier) &&
            Objects.equals(rowNumber, that.rowNumber) &&
            Objects.equals(cat, that.cat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, identifier, rowNumber, cat);
    }
}
