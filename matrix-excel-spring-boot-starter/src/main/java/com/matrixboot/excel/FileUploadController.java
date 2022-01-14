//package com.matrixboot.excel;
//
//import com.github.spring.boot.param.pojo.ExcelDataDTO;
//import com.github.spring.boot.param.pojo.FileInfo;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
///**
// * TODO
// * <p>
// * create in 2021/2/19 2:13 下午
// *
// * @author shishaodong
// * @version 0.0.1
// */
//@Slf4j
//@RestController
//public class FileUploadController {
//
//    private static final String PATH = "/Users/shishaodong/IdeaProjects/spring-boot-examples/spring-boot-param-binding/src/main/resources/file/";
//
//    @PostMapping("/file")
//    public FileInfo upload(MultipartFile file) throws IOException {
//        log.info("{}:{}:{}", file.getName(), file.getOriginalFilename(), file.getSize());
//        File localFile = new File(PATH, System.currentTimeMillis() + ".txt");
//        // 把上传的文件写到本地文件
//        file.transferTo(localFile);
//        return new FileInfo(PATH);
//    }
//
//    @PostMapping("/excel")
//    public List<ExcelDataDTO> upload(@ExcelRequestBody List<ExcelDataDTO> excel) {
//        return excel;
//    }
//
//}
