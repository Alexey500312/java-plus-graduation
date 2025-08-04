package ewm.pageble;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;

public abstract class AbstractPageOffset implements Pageable, Serializable {
    private static final long serialVersionUID = 1316878574569486977L;
    private final long offset;
    private final int pageSize;

    public AbstractPageOffset(long offset, int pageSize) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must not be less than zero");
        } else if (pageSize < 1) {
            throw new IllegalArgumentException("Page size must not be less than one");
        } else {
            this.offset = offset;
            this.pageSize = pageSize;
        }
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageNumber() {
        return (int) (this.offset / this.pageSize);
    }

    public long getOffset() {
        return this.offset;
    }

    public boolean hasPrevious() {
        return this.getPageNumber() > 0;
    }

    public Pageable previousOrFirst() {
        return this.hasPrevious() ? this.previous() : this.first();
    }

    public abstract Pageable next();

    public abstract Pageable previous();

    public abstract Pageable first();

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + Long.hashCode(this.offset);
        result = prime * result + this.pageSize;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            AbstractPageOffset other = (AbstractPageOffset) obj;
            return this.offset == other.offset && this.pageSize == other.pageSize;
        } else {
            return false;
        }
    }
}
