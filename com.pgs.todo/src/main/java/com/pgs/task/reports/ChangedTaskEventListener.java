package com.pgs.task.reports;

import com.pgs.task.event.TaskDone;
import com.pgs.task.event.TaskEvent;
import com.pgs.task.event.TaskUnDone;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangedTaskEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangedTaskEventListener.class);
    private final PersistedTaskEventRepository persistedTaskEventRepository;

    @Async
    @EventListener
    public void on(TaskDone event) {
        onChanged(event);
    }

    @Async
    @EventListener
    public void on(TaskUnDone event) {
        LOGGER.info("Got" + event);
        onChanged(event);
    }

    private void onChanged(final TaskEvent event) {
        LOGGER.info("Got" + event);
        persistedTaskEventRepository.save(new PersistedTaskEvent(event));
    }
}
