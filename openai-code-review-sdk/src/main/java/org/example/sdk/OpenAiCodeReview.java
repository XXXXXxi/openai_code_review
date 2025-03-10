package org.example.sdk;


import org.example.sdk.domain.service.OpenAiCodeReviewService;
import org.example.sdk.infrastructure.git.GitCommand;
import org.example.sdk.infrastructure.openai.IOpenAi;
import org.example.sdk.infrastructure.openai.impl.ChatGLM;
import org.example.sdk.infrastructure.weixin.WeiXin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAiCodeReview {

    private static final Logger logger = LoggerFactory.getLogger(OpenAiCodeReview.class);

    // 配置配置
    private String weixin_appid = "wx22de26a8a50cadba";
    private String weixin_secret = "63a72f9b5e8544365082feb646e5b858";
    private String weixin_touser = "oVU347BTSRAKPrLMCyVey7NjpXDg";
    private String weixin_template_id = "f0NaNpPms63CkI7XU4mj3ts-RuCk0UELUn_fJS81Tu0";

    // ChatGLM 配置
    private String chatglm_apiHost = "https://open.bigmodel.cn/api/paas/v4/chat/completions";
    private String chatglm_apiKeySecret = "";

    // Github 配置
    private String github_review_log_uri;
    private String github_token;

    // 工程配置 - 自动获取
    private String github_project;
    private String github_branch;
    private String github_author;


    public static void main(String[] args) throws Exception {
        GitCommand gitCommand = new GitCommand(
                getEnv("CODE_REVIEW_LOG_URI"),
                getEnv("CODE_TOKEN"),
                getEnv("COMMIT_PROJECT"),
                getEnv("COMMIT_BRANCH"),
                getEnv("COMMIT_AUTHOR"),
                getEnv("COMMIT_MESSAGE")
        );

        WeiXin weiXin = new WeiXin(
                getEnv("WEIXIN_APPID"),
                getEnv("WEIXIN_SECRET"),
                getEnv("WEIXIN_TOUSER"),
                getEnv("WEIXIN_TEMPLATE_ID")
        );

        IOpenAi openAi = new ChatGLM(
                getEnv("CHATGLM_APIHOST"),
                getEnv("CHATGLM_APIKEYSECRET")
        );

        OpenAiCodeReviewService openAiCodeReviewService = new OpenAiCodeReviewService(gitCommand, openAi, weiXin);
        openAiCodeReviewService.exec();

        logger.info("openai-code-review done!");
    }

    public static String getEnv(String key) {
        String value = System.getenv(key);
        if (null == value || value.isEmpty()) {
            throw new RuntimeException("key: " + key + " value is null");
        }
        return value;
    }
//    private static void pushMessage(String logUrl) {
//        String accessToken = WXAccessTokenUtils.getAccessToken();
//        System.out.println(accessToken);
//
//        TemplateMessageDTO message = new TemplateMessageDTO();
//        message.put("project","big-market");
//        message.put("review",logUrl);
//        message.setUrl(logUrl);
//
//        System.out.println("message: " + JSON.toJSONString(message));
//        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);
//        sendPostRequest(url, JSON.toJSONString(message));
//    }
//
//    private static void sendPostRequest(String urlString, String jsonBody) {
//        try {
//            URL url = new URL(urlString);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json; utf-8");
//            conn.setRequestProperty("Accept", "application/json");
//            conn.setDoOutput(true);
//
//            try (OutputStream os = conn.getOutputStream()) {
//                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            }
//
//            try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
//                String response = scanner.useDelimiter("\\A").next();
//                System.out.println(response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String codeReview(String diffCode) throws Exception{
//        String apiKeySecret = "478adcffd3ff471db48dcb95e7664572.kVLYjIS7Gwn6vOfP";
//        String token = BearerTokenUtils.getToken(apiKeySecret);
//
//        URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Authorization","Bearer " + token);
//        connection.setRequestProperty("Content-Type","application/json");
//        connection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//        connection.setDoOutput(true);
//
//        ChatCompletionRequestDTO chatCompletionRequest = new ChatCompletionRequestDTO();
//        chatCompletionRequest.setModel(Model.GLM_4_FLASH.getCode());
//        chatCompletionRequest.setMessages(new ArrayList<ChatCompletionRequestDTO.Prompt>() {{
//            add(new ChatCompletionRequestDTO.Prompt("user","\"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: \""));
//            add(new ChatCompletionRequestDTO.Prompt("user",diffCode));
//        }});
//
//        try (OutputStream os = connection.getOutputStream()){
//            byte[] input = JSON.toJSONString(chatCompletionRequest).getBytes(StandardCharsets.UTF_8);
//            os.write(input);
//        }
//
//        int responseCode = connection.getResponseCode();
//        System.out.println(responseCode);
//
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//
//        StringBuilder content = new StringBuilder();
//
//        while((inputLine = bufferedReader.readLine())!= null) {
//            content.append(inputLine);
//        }
//
//        bufferedReader.close();
//        connection.disconnect();
//
//        ChatCompletionSyncResponseDTO response = JSON.parseObject(content.toString(), ChatCompletionSyncResponseDTO.class);
//        System.out.println(response.getChoices().get(0).getMessage().getContent());
//
//        return response.getChoices().get(0).getMessage().getContent();
//    }
//
//    private static String writeLog(String token,String log) throws Exception {
//
//        Git git = Git.cloneRepository()
//                .setURI("https://github.com/XXXXXxi/openai-code-review-log.git")
//                .setDirectory(new File("repo"))
//                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
//                .call();
//
//        String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        File dateFolder = new File("repo/" + dateFolderName);
//        if (!dateFolder.exists()) {
//            dateFolder.mkdirs();
//        }
//
//
//        String fileName = new Random().nextLong() + ".md";
//        File file = new File(dateFolder, fileName);
//
//        try (FileWriter writer = new FileWriter(file)){
//            writer.write(log);
//        }
//
//        git.add().addFilepattern(dateFolderName + "/" + fileName).call();
//        git.commit().setMessage("Add new File").call();
//        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, "")).call();
//
//        return "https://github.com/XXXXXxi/openai-code-review-log/blob/master/" + dateFolderName + "/" + fileName;
//    }
}
