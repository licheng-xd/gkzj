package com.nuptsast.service;

import com.nuptsast.model.Answer;

import java.util.Map;

public interface AnswerService {
    Answer saveAnswer(Long userId, Long questionId, String answer);

    Map<Long, String> findAnswerByUserId(Long userId);
}
