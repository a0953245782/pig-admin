package com.chenym.pig.web;

import com.chenym.pig.utils.ImageCode;
import com.chenym.pig.utils.ImageCodeGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ValidateCodeController {

    @GetMapping("/code")
    public void createCode(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        ImageCode imageCode = ImageCodeGenerator.generate(new ServletWebRequest(request));
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());
    }

}
