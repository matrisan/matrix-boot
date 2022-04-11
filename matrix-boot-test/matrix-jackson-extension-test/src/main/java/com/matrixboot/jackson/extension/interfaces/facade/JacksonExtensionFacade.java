package com.matrixboot.jackson.extension.interfaces.facade;

import com.matrixboot.jackson.extension.interfaces.vo.UserInfo1;
import com.matrixboot.jackson.extension.interfaces.vo.UserInfo2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * create in 2022/4/11 23:44
 *
 * @author shishaodong
 * @version 0.0.1
 */
@RestController
public class JacksonExtensionFacade {

    @PostMapping("user1")
    public UserInfo1 user1(@RequestBody UserInfo1 user) {
        return user;
    }

    @PostMapping("user2")
    public UserInfo2 user2(@RequestBody UserInfo2 user) {
        return user;
    }

}
