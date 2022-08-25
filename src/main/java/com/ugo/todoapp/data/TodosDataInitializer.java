package com.ugo.todoapp.data;


import com.ugo.todoapp.core.todos.domain.Todo;
import com.ugo.todoapp.core.todos.domain.TodoRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "todo.data.init" , havingValue = "true")
public class TodosDataInitializer implements InitializingBean , ApplicationRunner , CommandLineRunner {

    private final TodoRepository todoRepository;

    public TodosDataInitializer(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        todoRepository.save(Todo.create("할일 1 "));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        todoRepository.save(Todo.create("할일 2"));
    }

    @Override
    public void run(String... args) throws Exception {
        todoRepository.save(Todo.create("할일 3"));
    }
}
