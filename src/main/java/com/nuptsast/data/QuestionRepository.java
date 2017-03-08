package com.nuptsast.data;

import com.nuptsast.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDepartment(String department);

    List<Question> findByQuestionContaining(String question);
}
