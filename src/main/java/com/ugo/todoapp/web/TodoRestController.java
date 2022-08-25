package com.ugo.todoapp.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.ugo.todoapp.core.todos.application.TodoEditor;
import com.ugo.todoapp.core.todos.application.TodoFinder;
import com.ugo.todoapp.core.todos.domain.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoRestController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TodoFinder todoFinder;
    private final TodoEditor todoEditor;

    public TodoRestController(TodoFinder todoFinder, TodoEditor todoEditor) {
        this.todoFinder = todoFinder;
        this.todoEditor = todoEditor;
    }

    @GetMapping("")
    public List<Todo> list(){
        return todoFinder.getAll();
    }

    @PostMapping("")
    // 응답값 지정
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid WriteTodoCommand command){
        logger.debug("command : {}",command);
        todoEditor.create(command.getTitle());
    }

    @PutMapping("/{id}")
    public void update(@RequestBody @Valid WriteTodoCommand command,@PathVariable("id") Long id){
        logger.debug("update request ID : {} , command :{}", id ,command);
        todoEditor.update(id,command.getTitle(),command.isCompleted());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id ){
        todoEditor.delete(id);
    }


    static class WriteTodoCommand{
        @NotBlank
        @Size(min = 4 , max = 140)
        private String title;

        private boolean completed;

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "WriteTodoCommand{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }
}
