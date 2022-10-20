package com.code.pojo;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * @author GYJ
 */
public class FileOption {


    public String inputPath;


    public String outputPath;

    public FileOption() throws IOException {
        this.inputPath = new ClassPathResource("words/inputs/").getURL().getPath();
        this.outputPath = new ClassPathResource("words/outputs/").getURL().getPath();
    }
}
