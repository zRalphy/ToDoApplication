package com.pgs.task.event;

import com.pgs.task.model.Task;

import java.time.Clock;

public class TaskUnDone extends TaskEvent {
    TaskUnDone(Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
