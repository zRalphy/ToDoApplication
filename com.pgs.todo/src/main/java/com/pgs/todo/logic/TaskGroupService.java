package com.pgs.todo.logic;

import com.pgs.todo.model.TaskGroup;
import com.pgs.todo.model.projection.GroupReadModel;
import com.pgs.todo.model.projection.GroupWriteModel;
import com.pgs.todo.repository.TaskGroupRepository;
import com.pgs.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
@RequiredArgsConstructor
public class TaskGroupService {
    private final TaskGroupRepository taskGroupRepository;
    private final TaskRepository taskRepository;

    public GroupReadModel createGroup(GroupWriteModel source) {
        TaskGroup result = taskGroupRepository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAllGroup() {
        return taskGroupRepository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group has undone tasks. Done all the task first.");
        }
        TaskGroup result = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found."));
        result.setDone(!result.isDone());
    }
}
