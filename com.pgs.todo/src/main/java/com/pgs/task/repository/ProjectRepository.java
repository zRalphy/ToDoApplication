package com.pgs.task.repository;

import com.pgs.task.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
    List<Project> findAll();
    Optional<Project> findById(Integer id);
    Project save (Project entity);
}
