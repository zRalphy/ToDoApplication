package com.pgs.task.service;

import com.pgs.task.model.TaskGroup;
import com.pgs.task.repository.TaskGroupRepository;
import com.pgs.task.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {

    @Test
    @DisplayName("should throw when undone tasks")
    void toggleGroup_undoneTasks_throwIllegalStateException() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);
        //system under test
        var toTest = new TaskGroupService(null, mockTaskRepository);

        //when
        Throwable exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Group has undone");

        //when + then
        assertThatThrownBy(() -> toTest.toggleGroup(1))
                .isInstanceOf(IllegalStateException.class);

        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> toTest.toggleGroup(1));

    }

    @Test
    @DisplayName("should throw when no group for a given id")
    void toggleGroup_noGroupForAGivenId_throwIllegalArgumentException() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        //and
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.empty());
        //system under test
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        //when
        Throwable exception = catchThrowable(() -> toTest.toggleGroup(1));

        //then
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("given id not found");

        //when + then
        assertThatThrownBy(() -> toTest.toggleGroup(1))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> toTest.toggleGroup(1));
    }

    @Test
    @DisplayName("should toggle group")
    void toggleGroup_noThrowAnyException_And_ToggleGroup() {
        //given
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        //and
        var group = new TaskGroup();
        var beforeToggle = group.isDone();
        //and
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.findById(anyInt())).thenReturn(Optional.of(group));
        //system under test
        var toTest = new TaskGroupService(mockGroupRepository, mockTaskRepository);

        //when
        toTest.toggleGroup(1);

        //then
        assertThat(group.isDone()).isEqualTo(!beforeToggle);
    }

    private TaskRepository taskRepositoryReturning(final boolean result) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(result);
        return mockTaskRepository;
    }
}