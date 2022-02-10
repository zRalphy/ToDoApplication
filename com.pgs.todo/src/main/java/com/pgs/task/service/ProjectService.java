package com.pgs.task.service;

import com.pgs.task.model.projection.GroupReadModel;
import com.pgs.task.model.projection.GroupTaskWriteModel;
import com.pgs.task.model.projection.GroupWriteModel;
import com.pgs.task.TaskConfigurationProperties;
import com.pgs.task.model.Project;
import com.pgs.task.model.projection.ProjectWriteModel;
import com.pgs.task.repository.ProjectRepository;
import com.pgs.task.repository.TaskGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final TaskGroupService taskGroupService;
    private final TaskConfigurationProperties configurationProperties;

    public List<Project> readAllProject() {
        return projectRepository.findAll();
    }

    public Project save(final ProjectWriteModel toSave) {
        return projectRepository.save(toSave.toProject());
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!configurationProperties.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed.");
        }
        return projectRepository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                                var task = new GroupTaskWriteModel();
                                                task.setDescription(projectStep.getDescription());
                                                task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toList()));
                    return taskGroupService.createGroup(targetGroup, project);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found."));
    }
}
