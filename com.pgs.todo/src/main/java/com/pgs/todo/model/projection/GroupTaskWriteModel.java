package com.pgs.todo.model.projection;

import com.pgs.todo.model.Task;
import com.pgs.todo.model.TaskGroup;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupTaskWriteModel {

    private String description;
    private LocalDateTime deadline;

    public Task toTask(final TaskGroup group) {
       return new Task(description, deadline, group);
    }
}
