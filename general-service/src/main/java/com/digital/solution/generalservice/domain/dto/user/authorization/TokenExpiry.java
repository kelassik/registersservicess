package com.digital.solution.generalservice.domain.dto.user.authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenExpiry implements Delayed, Serializable {
    private static final long serialVersionUID = 8822877055923810554L;

    private long expiry;
    private String value;

    public TokenExpiry(String value, Date date) {
        this.value = value;
        this.expiry = date.getTime();
    }

    public int compareTo(@NotNull Delayed other) {
        int result = 0;
        if (this != other) {
            long diff = this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
            if (diff != 0L) {
                result = 1;
                if (diff < 0L) {
                    result = -1;
                }
            }
        }

        return result;
    }

    public long getDelay(@NotNull TimeUnit unit) {
        return this.expiry - System.currentTimeMillis();
    }
}
