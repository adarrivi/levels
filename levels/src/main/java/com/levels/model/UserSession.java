package com.levels.model;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * Represents a user session, with its creation date (useful for expiration
 * proposes)
 * 
 * @author adarrivi
 * 
 */
@NotThreadSafe
public class UserSession {

    // Defines the maximum session life span.
    private static final long MAX_LIFESPAN_MS = TimeUnit.MINUTES.toMillis(10);

    private String key;
    private Date creationDate;

    public UserSession(String key, Date creationDate) {
        this.key = key;
        this.creationDate = creationDate;
    }

    public String getKey() {
        return key;
    }

    public boolean hasExpired(Date currentDate) {
        return currentDate.getTime() - creationDate.getTime() > MAX_LIFESPAN_MS;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    /**
     * The equality (and hashCode) is defined only by session key (what is
     * supposed to be unique) , as it doesn't make sense to include the creation
     * date
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UserSession)) {
            return false;
        }
        UserSession other = (UserSession) obj;
        return Objects.equals(getKey(), other.getKey());
    }

}
