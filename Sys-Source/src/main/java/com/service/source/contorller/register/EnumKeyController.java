package com.service.source.contorller.register;

import com.service.source.utils.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/RegisterEnumKey")
public class EnumKeyController {
    @GetMapping("/hello")
    public ResponseResult hello() {
        return new ResponseResult(200, "hello");
    }
}
