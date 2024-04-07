package org.zalex.domain;

import java.util.Objects;

public class Purpose {

    private final String value;

    public Purpose(final String value) throws DomainObjectCreationException {
        if (value == null || value.length() < 50) {
            throw new DomainObjectCreationException("Purpose must contain at least 50 characters");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final Purpose purpose)) return false;
        return Objects.equals(value, purpose.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Purpose{" +
                "value='" + value + '\'' +
                '}';
    }
}
