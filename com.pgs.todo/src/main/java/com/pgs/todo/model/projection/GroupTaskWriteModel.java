package com.pgs.todo.model.projection;

import com.pgs.todo.model.Task;
import com.pgs.todo.model.TaskGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class GroupTaskWriteModel {
    @NotBlank(message = "Task's description must not be empty")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime deadline;

    public Task toTask(final TaskGroup group) {
       return new Task(description, deadline, group);
    }
}
