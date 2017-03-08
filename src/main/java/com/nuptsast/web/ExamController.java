package com.nuptsast.web;

import com.nuptsast.Config;
import com.nuptsast.model.Question;
import com.nuptsast.model.User;
import com.nuptsast.service.AnswerService;
import com.nuptsast.service.QuestionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ExamController {
    private final QuestionService questionService;

    private final AnswerService answerService;

    private Random random = new Random();

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private Config config;

    @Autowired
    public ExamController(QuestionService questionService,
        AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @RequestMapping(value = "/image/{name}", method = RequestMethod.GET)
    public void getImage(@PathVariable String name, HttpServletResponse response) {
        FileInputStream fis = null;
        try {
            OutputStream out = response.getOutputStream();
            File f = new File(config.getImagePath() + "/" + name + ".jpg");
            if (!f.exists()) {
                return;
            }
            new FileReader(f).read();
            response.setContentType("image/jpeg");
            response.addHeader("Content-Disposition", "attachment");

            fis = new FileInputStream(f);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            out.write(b);
            out.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public String selectQuestion(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || StringUtils.isEmpty(user.getTargetDepartment())) {
            // session.setAttribute("status", Boolean.FALSE);
            return "redirect:/profile";
        }
        if (session.getAttribute("questions") == null) {
            logger.info(user);
            List questions = questionService.getQuestions(user.getTargetDepartment());
            if (questions.isEmpty()) {
                session.setAttribute("status", Boolean.FALSE);
                return "redirect:/profile";
            }
            // 随机取20个
            List questionList = new ArrayList<>();
            if (questionList.size() > config.getOnceCount()) {
                for (int i=0; i<config.getOnceCount(); i++) {
                    questionList.add(questions.get(Math.abs(random.nextInt(questions.size()))));
                }
            } else {
                questionList = questions;
            }

            session.setAttribute("questions", questionList);
            /*session.setAttribute("finished",
                answerService.findAnswerByUserId(user.getId()));*/
            session.setAttribute("finished", new HashMap<Long, String>());
        }
        return "redirect:/exam/0";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/exam/{questionId}", method = RequestMethod.GET)
    public String showQuestion(@PathVariable Integer questionId, Model model,
        HttpSession session) {
        List<Question> questions = (List<Question>) session
            .getAttribute("questions");
        if (questions == null) {
            return "redirect:/exam";
        }
        model.addAttribute("question", questions.get(questionId));
        model.addAttribute("questionCount", questions.size());
        model.addAttribute("questionId", questionId);
        Map<Long, String> finished = (Map<Long, String>) session
            .getAttribute("finished");
        if (finished.get(questions.get(questionId).getId()) == null) {
            session.removeAttribute("status");
        } else {
            if (questions.get(questionId).getTip().equalsIgnoreCase(finished.get(questions.get(questionId).getId()))) {
                model.addAttribute("status", true);
            } else {
                model.addAttribute("status", false);
            }
        }
        return "exam";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/exam/{questionId}", method = RequestMethod.POST)
    public String submitQuestion(@PathVariable Long questionId,
        @RequestParam("answer") String answer,
        @RequestParam("submit") String direct,
        HttpSession session) {
        if (Objects.equals(direct, "next")) {
            return "redirect:/exam/" + (questionId + 1);
        } else if (Objects.equals(direct, "prev")) {
            return "redirect:/exam/" + (questionId - 1);
        }
        User user = (User) session.getAttribute("user");
        List<Question> questions = (List<Question>) session
            .getAttribute("questions");
        Map<Long, String> finished = (Map<Long, String>) session
            .getAttribute("finished");
        answerService.saveAnswer(user.getId(),
            questions.get(questionId.intValue()).getId(), answer);
        finished.put(questions.get(questionId.intValue()).getId(), answer);
        session.setAttribute("finished", finished);
        // 判断对错
        /*if (questions.get(questionId.intValue()).getResult().equalsIgnoreCase(answer)) {
            session.setAttribute("status", true);
        } else {
            session.setAttribute("status", false);
        }*/
        return "redirect:/exam/" + questionId;
    }
}
