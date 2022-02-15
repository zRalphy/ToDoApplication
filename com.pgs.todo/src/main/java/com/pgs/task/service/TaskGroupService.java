package com.pgs.task.service;

import com.pgs.task.model.TaskGroup;
import com.pgs.task.model.projection.GroupReadModel;
import com.pgs.task.model.projection.GroupWriteModel;
import com.pgs.task.model.Project;
import com.pgs.task.repository.TaskGroupRepository;
import com.pgs.task.repository.TaskRepository;
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
        return createGroup(source, null);
    }

    GroupReadModel createGroup(final GroupWriteModel source, final Project project) {
        TaskGroup result = taskGroupRepository.save(source.toGroup(project));
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
        taskGroupRepository.save(result);
    }
}
