package com.nuptsast.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String question; // 问题内容

    @OneToMany(cascade = CascadeType.ALL)
    private List<Choice> choices; // 问题选项

    @NotNull
    private String department; // 问题分类

    @NotNull
    private String result; // 答案

    private String image; // 图片

    private String tip; // 错误提示

    public Question(String question, List<Choice> choices, String department) {
        this.question = question;
        this.choices = choices;
        this.department = department;
    }

    public Question(String question,
        List<Choice> choices, String department, String result,
        String tip) {
        this.question = question;
        this.choices = choices;
        this.department = department;
        this.result = result;
        this.tip = tip;
    }

    public Question() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
