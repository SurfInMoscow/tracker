package ru.vorobyev.tracker.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionBean {

    private String message;

    public ExceptionBean(String message) {
        this.message = message;
    }
}