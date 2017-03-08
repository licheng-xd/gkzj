package com.nuptsast.service;

import com.nuptsast.model.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface QuestionService {
    Question addQuestion(Question question);

    List<Question> getQuestions(String department);

    void removeQuestion(Long id);

    List<Question> findQuestionContaining(String question);

    Boolean importFile(InputStream file) throws IOException;
}
