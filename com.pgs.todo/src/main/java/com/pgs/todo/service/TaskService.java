package com.pgs.todo.service;

import com.pgs.todo.model.Task;
import com.pgs.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
    private final TaskRepository taskRepository;

    @Async
    public CompletableFuture<List<Task>> findAllAsync() {
        LOGGER.info("Supply async!");
        return CompletableFuture.supplyAsync(taskRepository::findAll);
    }
}
