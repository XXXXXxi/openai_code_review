package org.example.sdk.infrastructure.weixin;

import com.alibaba.fastjson2.JSON;
import org.example.sdk.infrastructure.weixin.dto.TemplateMessageDTO;
import org.example.sdk.types.utils.WXAccessTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;

public class WeiXin {

    private final Logger logger = LoggerFactory.getLogger(WeiXin.class);

    private final String appid;

    private final String secret;

    private final String touser;

    private final String template_id;

    public WeiXin(String appid, String secret, String touser, String templateId) {
        this.appid = appid;
        this.secret = secret;
        this.touser = touser;
        this.template_id = templateId;
    }

    public void sendTemplateMessage(String logUrl, Map<String, Map<String,String>> data) throws Exception {
        String accessToken = WXAccessTokenUtils.getAccessToken(appid,secret);

        logger.info("appid: " + appid + " template_id: " + template_id + " logUrl: " + logUrl);

        TemplateMessageDTO templateMessageDTO = new TemplateMessageDTO(touser, template_id);;
        templateMessageDTO.setData(data);
        templateMessageDTO.setUrl(logUrl);
        logger.info("openai-code-review weixin template message! {}",JSON.toJSONString(templateMessageDTO));
        String urlString = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = JSON.toJSONString(templateMessageDTO).getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
            String response = scanner.useDelimiter("\\A").next();
            logger.info("openai-code-review weixin template message done! {}",response);
        }
    }
}
