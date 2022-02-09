package com.matrixboot.idempotent.config;

import com.matrixboot.idempotent.core.AbstractIdempotentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * create in 2022/1/12 11:17 AM
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Slf4j
@RestController
@RequestMapping("/matrixboot/idempotent")
public class IdempotentController {

    @Resource
    private AbstractIdempotentService idempotentService;

    /**
     * 获取 token
     *
     * @return String
     */
    @GetMapping("/token")
    String token() {
        log.debug("获取 token");
        return idempotentService.generateToken();
    }

}
