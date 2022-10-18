package com.code.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author GYJ
 */
@Component
@Data
public class FileOption {

    @Value("${file.input-path}")
    public String inputPath;

    @Value("${file.output-path}")
    public String outputPath;

}
