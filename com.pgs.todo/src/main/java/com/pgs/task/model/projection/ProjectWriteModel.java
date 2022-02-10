package com.pgs.task.model.projection;

import com.pgs.task.model.Project;
import com.pgs.task.model.ProjectStep;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
@Getter
@Setter
public class ProjectWriteModel {

    @NotBlank(message = "Project's description must not be empty")
    private String description;

    @Valid
    private List<ProjectStep> steps = new ArrayList<>();

    public ProjectWriteModel() {
        steps.add(new ProjectStep());
    }

    public Project toProject() {
       var result = new Project();
       result.setDescription(description);
       steps.forEach(step -> step.setProject(result));
       result.setSteps(new HashSet<>(steps));
       return result;
    }
}
