package ru.vorobyev.tracker.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "checkendpoint")
@Getter
@Setter
public class RestClient {
    private String url;
    private String basePath;
}
