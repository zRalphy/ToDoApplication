package com.pgs.task;

import com.pgs.task.model.Task;
import com.pgs.task.model.TaskGroup;
import com.pgs.task.repository.TaskGroupRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
class Warmup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Warmup.class);
    private final TaskGroupRepository taskGroupRepository;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        LOGGER.info("Application warmup after context refreshed");
        final String description = "ApplicationContextEvent";
        if (!taskGroupRepository.existsByDescription(description)) {
            LOGGER.info("No required group found! Adding it!");
            var group = new TaskGroup();
            group.setDescription(description);
            group.setTasks(Set.of(
                    new Task("ContextClosedEvent", null, group),
                    new Task("ContextRefreshedEvent", null, group),
                    new Task("ContextStoppedEvent", null, group),
                    new Task("ContextStartedEvent", null, group)
            ));
            taskGroupRepository.save(group);
        }
    }
}
