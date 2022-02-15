package com.pgs.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgs.task.model.Task;
import com.pgs.task.repository.TaskRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void httpGet_returnGivenTask() throws Exception {
        //given
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        //when + then
        mockMvc.perform(get("/tasks/" + id)).andExpect(status().is2xxSuccessful());

    }

    @Test
    void httpGet_returnAllTask() throws Exception {
        //given
        List<Task> tasks = List.of(new Task("foo1", LocalDateTime.now()),
                new Task("foo2", LocalDateTime.now()));

        repo.save(tasks.get(0));
        repo.save(tasks.get(1));

        //when + then
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", equalTo(tasks.get(0).getId())))
                .andExpect(jsonPath("$[0].description", equalTo(tasks.get(0).getDescription())))
                .andExpect(jsonPath("$[1].id", equalTo(tasks.get(1).getId())))
                .andExpect(jsonPath("$[1].description", equalTo(tasks.get(1).getDescription())));
    }

    @Test
    @SneakyThrows
    void httpPost_returnTask() {
        //given
        Task taskToCreate = repo.save(new Task("fooTestPost", LocalDateTime.now()));

        //when + then
        mockMvc.perform(post("/tasks")
                        .content(objectMapper.writeValueAsString(taskToCreate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.description", equalTo(taskToCreate.getDescription())));
    }

    @Test
    @SneakyThrows
    void httpPut_returnNoContent() {
        //given
        Task taskToUpdate = repo.save(new Task("fooTestPut", LocalDateTime.now()));

        //when + then
        mockMvc.perform(put("/tasks/" + taskToUpdate.getId())
                        .content(objectMapper.writeValueAsString(taskToUpdate))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
