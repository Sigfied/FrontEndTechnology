package com.code.controller;

import com.code.pojo.FileOption;
import com.deepoove.poi.XWPFTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author GYJ
 */
@CrossOrigin(origins = {"*","null"})
@Controller
@RequestMapping("/file")
public class FileController {

    private static final Logger log =  LoggerFactory.getLogger(FileController.class);

    private final FileOption fileOption;


    public FileController() throws IOException {
        this.fileOption  = new FileOption();
    }

    @PostMapping("upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        if(file == null){
            log.info("File is null");
        }
        assert file != null;
        log.info("Uploading file " + file.getOriginalFilename());
        String fileName = file.getOriginalFilename();
        File dest = new File(fileOption.inputPath +'/'+ fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        String info =   "upload success";
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.info("Failed to transfer file "+file);
            info = "error file";
            return info;
        }
        log.info("Upload success");
        return info;
    }

    public  static void fileDownLoad(HttpServletResponse response, FileOption fileOption){
        File file = new File(fileOption.outputPath + "output.docx");
        if(!file.exists()){
            return;
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Content-Disposition", "attachment;filename=" + "output.docx" );
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            log.error("{e}",e);
        }
    }

    @PostMapping("test")
    public void getTestInfo(@RequestBody Map<String,Object> info,HttpServletResponse response) throws IOException {
        LocalDateTime time = LocalDateTime.now();
        int year = time.getYear();
        int month = time.getMonth().getValue();
        int day = time.getDayOfMonth();
        String studentNo = (String) info.get("studentNo");
        String studentName = (String) info.get("studentName");
        String testName = (String) info.get("testName");
        List<String> purpose = castList(info.get("purpose"),String.class);
        List<String>  task = castList(info.get("task"),String.class);
        List<String>  result = castList(info.get("result"),String.class);
        String experience = (String) info.get("experience");
        log.info("{},{},{},{},{},{},{}", studentNo, studentName, testName,purpose, task,result,experience);
        String absolutePath = fileOption.inputPath+"template.docx";
        HashMap<String, Object> rs = new HashMap<String,Object>(16) {
            {
                put("year", year);
                put("month", month);
                put("day", day);
                put("studentNo", studentNo);
                put("studentName", studentName);
                put("testName", testName);
                put("purpose", purpose);
                put("task", task);
                put("result", result);
                put("experience", experience);
            }
        };
        XWPFTemplate template = XWPFTemplate.compile(absolutePath).render(rs);
        template.writeAndClose(new FileOutputStream(fileOption.outputPath+"output.docx"));
        fileDownLoad(response,fileOption);
        FileSystemUtils.deleteRecursively(new File(fileOption.outputPath+"output.docx"));
        template.close();
//        return rs;
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }


}
