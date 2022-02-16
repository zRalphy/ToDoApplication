package com.pgs.task.event;

import com.pgs.task.model.Task;
import lombok.Getter;

import java.time.Clock;
import java.time.Instant;

@Getter
public abstract class TaskEvent {

    public static TaskEvent changed(Task source) {
        return source.isDone() ? new TaskDone(source) : new TaskUnDone(source);
    }

    private final int taskId;
    private final Instant occurrence;

    TaskEvent(final int taskId, Clock clock) {
        this.taskId = taskId;
        this.occurrence = Instant.now(clock);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "taskId=" + taskId +
                ", occurrence=" + occurrence +
                '}';
    }
}
