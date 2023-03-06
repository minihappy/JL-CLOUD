package com.authorization.service.contorller;


import com.alibaba.fastjson.JSONObject;
import com.authorization.service.utils.RedisCache;
import com.authorization.service.utils.Result;
import com.google.code.kaptcha.Producer;

//import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;

//@Api(value = "验证码")
@RestController
public class captcha {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private Producer producer;

    //    @ApiOperation(value = "获取验证码", notes = "根据uuid，以及Producer生成的验证码")
    @GetMapping("/captcha")
    public Result getCaptcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();
        BufferedImage image = producer.createImage(code);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());
        redisCache.setCacheMapValue("captcha", key, code);
        redisCache.expire("captcha", 180);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", key);
        jsonObject.put("captcha", base64Img);
        return Result.success(jsonObject);
    }
}
