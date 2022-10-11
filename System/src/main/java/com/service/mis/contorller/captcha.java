package com.service.mis.contorller;

import com.alibaba.fastjson.JSONObject;
import com.service.mis.utils.RedisCache;
import com.service.mis.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.code.kaptcha.Producer;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
public class captcha {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private Producer producer;

    @GetMapping("/captcha")
    public ResponseResult getCaptcha() throws IOException {
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
        return new ResponseResult(200, "成功", jsonObject);
    }
}
