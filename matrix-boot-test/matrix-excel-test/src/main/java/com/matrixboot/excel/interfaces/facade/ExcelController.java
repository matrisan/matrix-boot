package com.matrixboot.excel.interfaces.facade;

import com.matrixboot.excel.annotation.ExcelRequestBody;
import com.matrixboot.excel.interfaces.vo.ExcelDataDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * create in 2022/4/12 15:05
 *
 * @author shishaodong
 * @version 0.0.1
 */
@RestController
public class ExcelController {

    @PostMapping("/excel")
    public List<ExcelDataDTO> upload(@ExcelRequestBody List<ExcelDataDTO> excel) {
        return excel;
    }

}
