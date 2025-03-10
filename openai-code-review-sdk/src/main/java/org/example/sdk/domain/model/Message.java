package org.example.sdk.domain.model;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private String touser = "oVU347BTSRAKPrLMCyVey7NjpXDg";
    private String template_id = "GLlAM-RdtKXXfwt2srroR7BUp0IhGBR4JR7e4uHCSwe5CDgmo";
    private String url = "https://www.baidu.com";
    private Map<String, Map<String, String>> data = new HashMap<>();

    public void put(String key, String value) {
        data.put(key, new HashMap<String, String>() {
            private static final long serialVersionUID = 7092338402387318563L;

            {
                put("value", value);
            }
        });
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

}
