package com.krzysiek.recruiting.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@ConfigurationProperties("storage")
@Getter
@Setter
public class StorageProperties {
    private String location;
    private List<String> allowedExtensions;
}
