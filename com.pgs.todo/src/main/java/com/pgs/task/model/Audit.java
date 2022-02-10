package com.pgs.task.model;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable
class Audit {
    private LocalDateTime created;
    private LocalDateTime updated;

    @PrePersist
    void prePersist() {
        created = LocalDateTime.now();
    }

    @PreUpdate
    void preMarge() {
        updated = LocalDateTime.now();
    }
}
