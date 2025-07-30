package ewm.pageble;

import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class PageOffset extends AbstractPageOffset {
    private static final long serialVersionUID = 3173327515679479122L;
    private final Sort sort;

    protected PageOffset(long offset, int pageSize, Sort sort) {
        super(offset, pageSize);
        Assert.notNull(sort, "Sort must not be null");
        this.sort = sort;
    }

    public static PageOffset of(long offset, int pageSize) {
        return of(offset, pageSize, Sort.unsorted());
    }

    public static PageOffset of(long offset, int pageSize, Sort sort) {
        return new PageOffset(offset, pageSize, sort);
    }

    public static PageOffset of(long offset, int pageSize, Sort.Direction direction, String... properties) {
        return of(offset, pageSize, Sort.by(direction, properties));
    }

    public static PageOffset ofSize(int pageSize) {
        return of(0, pageSize);
    }

    public Sort getSort() {
        return this.sort;
    }

    public PageOffset next() {
        return new PageOffset(this.getOffset() + this.getPageSize(), this.getPageSize(), this.getSort());
    }

    public PageOffset previous() {
        return this.getPageNumber() == 0
                ? (this.getOffset() == 0 ? this : new PageOffset(0L, this.getPageSize(), this.getSort()))
                : new PageOffset(this.getOffset() - this.getPageSize(), this.getPageSize(), this.getSort());
    }

    public PageOffset first() {
        return new PageOffset(0L, this.getPageSize(), this.getSort());
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof PageOffset)) {
            return false;
        } else {
            PageOffset that = (PageOffset) obj;
            return super.equals(that) && this.sort.equals(that.sort);
        }
    }

    public PageOffset withPage(int offset) {
        return new PageOffset(offset, this.getPageSize(), this.getSort());
    }

    public PageOffset withSort(Sort.Direction direction, String... properties) {
        return new PageOffset(this.getOffset(), this.getPageSize(), Sort.by(direction, properties));
    }

    public PageOffset withSort(Sort sort) {
        return new PageOffset(this.getOffset(), this.getPageSize(), sort);
    }

    public int hashCode() {
        return 31 * super.hashCode() + this.sort.hashCode();
    }

    public String toString() {
        return String.format("Page offset [offset: %d, size %d, sort: %s]", this.getOffset(), this.getPageSize(), this.sort);
    }
}
