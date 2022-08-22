package com.ugo.todoapp.core.todos.infrastructure;

import com.ugo.todoapp.Constant;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ugo.todoapp.core.todos.domain.Todo;
import com.ugo.todoapp.core.todos.domain.TodoRepository;

/**
 * Spring Data JPA 기반 할 일 저장소 구현체
 *
 * @author springrunner.kr@gmail.com
 */
@Profile(Constant.PROFILE_PRODUCTION)
public interface JpaTodoRepository extends TodoRepository, JpaRepository<Todo, Long> {

}
