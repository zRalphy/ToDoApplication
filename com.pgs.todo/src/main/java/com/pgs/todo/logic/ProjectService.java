package com.pgs.todo.logic;

import com.pgs.todo.model.Project;
import com.pgs.todo.model.Task;
import com.pgs.todo.model.TaskConfigurationProperties;
import com.pgs.todo.model.TaskGroup;
import com.pgs.todo.model.projection.GroupReadModel;
import com.pgs.todo.repository.ProjectRepository;
import com.pgs.todo.repository.TaskGroupRepository;
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
    private final TaskConfigurationProperties configurationProperties;

    public Project createProject(final Project source) {
        return projectRepository.save(source);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!configurationProperties.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed.");
        }
        TaskGroup result = projectRepository.findById(projectId)
                .map(project -> {
                    TaskGroup taskGroup = new TaskGroup();
                    taskGroup.setDescription(project.getDescription());
                    taskGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> new Task(
                                            projectStep.getDescription(),
                                            deadline.plusDays(projectStep.getDaysToDeadline())))
                                    .collect(Collectors.toSet()));
                    taskGroup.setProject(project);
                    return taskGroupRepository.save(taskGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found."));
        return new GroupReadModel(result);
    }

    public List<Project> readAllProject() {
        return projectRepository.findAll();
    }
}
