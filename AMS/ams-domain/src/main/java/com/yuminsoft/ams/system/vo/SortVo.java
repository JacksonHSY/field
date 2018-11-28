package com.yuminsoft.ams.system.vo;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 排序VO
 * Created by wulinjie on 2017/6/17.
 */
public class SortVo implements Iterable<SortVo.Order>, Serializable {

    private static final long serialVersionUID = -2587556595613792673L;

    public static final SortVo.Direction DEFAULT_DIRECTION; //  默认排序方向

    private final List<Order> orders;

    static {
        // 初始化排序方向 asc
        DEFAULT_DIRECTION = SortVo.Direction.ASC;
    }

    public SortVo(SortVo.Order... orders) {
        this(Arrays.asList(orders));
    }

    public SortVo(List<SortVo.Order> orders) {
        if (null != orders && !orders.isEmpty()) {
            this.orders = orders;

        } else {
            throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
        }
    }

    public SortVo(String... properties) {
        this(DEFAULT_DIRECTION, properties);
    }

    public SortVo(SortVo.Direction direction, String... properties) {
        this(direction, (List) (properties == null ? new ArrayList() : Arrays.asList(properties)));
    }

    public SortVo(SortVo.Direction direction, List<String> properties) {
        if (properties != null && !properties.isEmpty()) {
            this.orders = new ArrayList(properties.size());
            Iterator var3 = properties.iterator();

            while (var3.hasNext()) {
                String property = (String) var3.next();
                this.orders.add(new SortVo.Order(direction, property));
            }

        } else {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }
    }

    public SortVo and(SortVo sort) {
        if (sort == null) {
            return this;
        } else {
            ArrayList<SortVo.Order> these = new ArrayList(this.orders);
            Iterator var3 = sort.iterator();

            while (var3.hasNext()) {
                SortVo.Order order = (SortVo.Order) var3.next();
                these.add(order);
            }

            return new SortVo(these);
        }
    }

    public SortVo.Order getOrderFor(String property) {
        Iterator var2 = this.iterator();

        SortVo.Order order;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            order = (SortVo.Order) var2.next();
        } while (!order.getProperty().equals(property));

        return order;
    }

    @Override
    public Iterator<SortVo.Order> iterator() {
        return this.orders.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof SortVo)) {
            return false;
        }

        SortVo that = (SortVo) obj;

        return this.orders.equals(that.orders);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + orders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return StringUtils.collectionToCommaDelimitedString(this.orders);
    }

    public static class Order implements Serializable {
        private static final boolean DEFAULT_IGNORE_CASE = false;
        private final SortVo.Direction direction;
        private final String property;
        private final boolean ignoreCase;
        private final NullHandling nullHandling;

        public Order(SortVo.Direction direction, String property) {
            this(direction, property, false, (SortVo.NullHandling) null);
        }

        public Order(SortVo.Direction direction, String property, SortVo.NullHandling nullHandlingHint) {
            this(direction, property, false, nullHandlingHint);
        }

        public Order(String property) {
            this(SortVo.DEFAULT_DIRECTION, property);
        }

        private Order(SortVo.Direction direction, String property, boolean ignoreCase, SortVo.NullHandling nullHandling) {
            if (!StringUtils.hasText(property)) {
                throw new IllegalArgumentException("Property must not null or empty!");
            } else {
                this.direction = direction == null ? SortVo.DEFAULT_DIRECTION : direction;
                this.property = property;
                this.ignoreCase = ignoreCase;
                this.nullHandling = nullHandling == null ? SortVo.NullHandling.NATIVE : nullHandling;
            }
        }

        public SortVo.Direction getDirection() {
            return this.direction;
        }

        public String getProperty() {
            return this.property;
        }

        public boolean isAscending() {
            return this.direction.equals(SortVo.Direction.ASC);
        }

        public boolean isIgnoreCase() {
            return this.ignoreCase;
        }

        public SortVo.Order with(SortVo.Direction order) {
            return new SortVo.Order(order, this.property, this.nullHandling);
        }

        public SortVo withProperties(String... properties) {
            return new SortVo(this.direction, properties);
        }

        public SortVo.Order ignoreCase() {
            return new SortVo.Order(this.direction, this.property, true, this.nullHandling);
        }

        public SortVo.Order with(SortVo.NullHandling nullHandling) {
            return new SortVo.Order(this.direction, this.property, this.ignoreCase, nullHandling);
        }

        public SortVo.Order nullsFirst() {
            return this.with(SortVo.NullHandling.NULLS_FIRST);
        }

        public SortVo.Order nullsLast() {
            return this.with(SortVo.NullHandling.NULLS_LAST);
        }

        public SortVo.Order nullsNative() {
            return this.with(SortVo.NullHandling.NATIVE);
        }

        public SortVo.NullHandling getNullHandling() {
            return this.nullHandling;
        }

        @Override
        public int hashCode() {
            int result = 17;

            result = 31 * result + direction.hashCode();
            result = 31 * result + property.hashCode();
            result = 31 * result + (ignoreCase ? 1 : 0);
            result = 31 * result + nullHandling.hashCode();

            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            } else if (!(obj instanceof SortVo.Order)) {
                return false;
            } else {
                SortVo.Order that = (SortVo.Order) obj;
                return this.direction.equals(that.direction) && this.property.equals(that.property) && this.ignoreCase == that.ignoreCase && this.nullHandling.equals(that.nullHandling);
            }
        }

        @Override
        public String toString() {
            String result = String.format("%s: %s", new Object[]{this.property, this.direction});
            if (!SortVo.NullHandling.NATIVE.equals(this.nullHandling)) {
                result = result + ", " + this.nullHandling;
            }

            if (this.ignoreCase) {
                result = result + ", ignoring case";
            }

            return result;
        }
    }

    public static enum Direction {
        ASC,
        DESC;

        private Direction() {

        }

        public static SortVo.Direction fromString(String value) {
            try {
                return valueOf(value.toUpperCase(Locale.US));

            } catch (Exception var2) {
                throw new IllegalArgumentException(String.format("Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", new Object[]{value}), var2);
            }
        }

        public static SortVo.Direction fromStringOrNull(String value) {
            try {
                return fromString(value);
            } catch (IllegalArgumentException var2) {
                return null;
            }
        }

    }

    public static enum NullHandling {
        NATIVE,
        NULLS_FIRST,
        NULLS_LAST;
    }
}
