package com.nuptsast.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nuptsast.Config;
import com.nuptsast.model.Choice;
import com.nuptsast.model.Question;
import com.nuptsast.service.QuestionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping()
public class ManageController {
    private final QuestionService questionService;

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private Config config;

    @Autowired
    public ManageController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String manage(Model model) {
        model.addAttribute("question", new Question());
        return "manage";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String searchQuestion(@RequestParam("question") String question,
        Model model) {
        List<Question> questions = questionService
            .findQuestionContaining(question);
        model.addAttribute("questions", questions);
        return "manage";
    }

    @ResponseBody
    @RequestMapping(value = "/import", method = RequestMethod.POST)
    public ObjectNode importQuestion(@RequestParam(required = false) MultipartFile file,
        @RequestParam String department,
        @RequestParam String question,
        @RequestParam List<String> choices,
        @RequestParam String result,
        @RequestParam(required = false, defaultValue = "") String tip) {
        logger.info("question to be imported is " + question);
        List<Choice> choicesList = choices.stream().map(Choice::new)
            .collect(Collectors.toList());
        Question q = new Question(question, choicesList, department, result, tip);
        if (file != null) {
            // 存
            String image = UUID.randomUUID().toString();
            if (store(file, image)) {
                q.setImage(image);
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        questionService.addQuestion(q);
        node.put("status", true);
        return node;
    }

    private boolean store(MultipartFile file, String name) {
        try {
            file.transferTo(new File(config.getImagePath() + "/"
                + name + ".jpg"));
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @RequestMapping(value = "/importFile", method = RequestMethod.POST)
    public String uploadFile(
        @RequestParam("file") MultipartFile file) {
        logger.info(
            file.getContentType() + " " + file.getOriginalFilename() + " "
                + file.getSize());
        try {
            questionService.importFile(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/manage?error";
        }
        return "redirect:/manage";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteQuestion(@RequestParam("id") Long id) {
        questionService.removeQuestion(id);
        return "manage";
    }
}
