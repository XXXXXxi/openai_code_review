package org.example.sdk;

import com.alibaba.fastjson2.JSON;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.example.sdk.domain.model.ChatCompletionRequest;
import org.example.sdk.domain.model.ChatCompletionSyncResponse;
import org.example.sdk.domain.model.Model;
import org.example.sdk.types.utils.BearerTokenUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.stream.DoubleStream;

/**
 * @Classname OpenAiCodeReview
 * @Description TODO
 * @Date 2025/3/9 17:16
 * @Created by 12135
 */
public class OpenAiCodeReview {

    public static void main(String[] args) throws Exception {
        System.out.println("openai 代码评审，测试执行");

        String token = System.getenv("GITHUB_TOKEN");
        if (null == token || token.isEmpty()) {
            throw new RuntimeException("token is null");
        }

        // 1. 代码检出
        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        processBuilder.directory(new File("."));

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        StringBuilder diffCode = new StringBuilder();

        while((line= reader.readLine())!= null) {
            diffCode.append(line);
        }

        int exitCode = process.waitFor();
        System.out.println("Exited with code:" + exitCode);

        System.out.println("diff code： " + diffCode.toString());

        // 2. charglm 代码评审

        String log = codeReview(diffCode.toString());
        System.out.println("code review： " + log);

        // 3. 写入评审日志
        writeLog(token,log);

    }

    private static String codeReview(String diffCode) throws Exception{
        String apiKeySecret = "478adcffd3ff471db48dcb95e7664572.kVLYjIS7Gwn6vOfP";
        String token = BearerTokenUtils.getToken(apiKeySecret);

        URL url = new URL("https://open.bigmodel.cn/api/paas/v4/chat/completions");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization","Bearer " + token);
        connection.setRequestProperty("Content-Type","application/json");
        connection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setDoOutput(true);

        ChatCompletionRequest chatCompletionRequest = new ChatCompletionRequest();
        chatCompletionRequest.setModel(Model.GLM_4_FLASH.getCode());
        chatCompletionRequest.setMessages(new ArrayList<ChatCompletionRequest.Prompt>() {{
            add(new ChatCompletionRequest.Prompt("user","\"你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审。代码为: \""));
            add(new ChatCompletionRequest.Prompt("user",diffCode));
        }});

        try (OutputStream os = connection.getOutputStream()){
            byte[] input = JSON.toJSONString(chatCompletionRequest).getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int responseCode = connection.getResponseCode();
        System.out.println(responseCode);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;

        StringBuilder content = new StringBuilder();

        while((inputLine = bufferedReader.readLine())!= null) {
            content.append(inputLine);
        }

        bufferedReader.close();
        connection.disconnect();

        ChatCompletionSyncResponse response = JSON.parseObject(content.toString(), ChatCompletionSyncResponse.class);
        System.out.println(response.getChoices().get(0).getMessage().getContent());

        return response.getChoices().get(0).getMessage().getContent();
    }

    private static String writeLog(String token,String log) throws Exception {

        Git git = Git.cloneRepository()
                .setURI("https://github.com/XXXXXxi/openai-code-review-log")
                .setDirectory(new File("repo"))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""))
                .call();

        String dateFolderName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File dateFolder = new File("repo/" + dateFolderName);
        if (!dateFolder.exists()) {
            dateFolder.mkdirs();
        }


        String fileName = new Random().nextLong() + ".md";
        File file = new File(dateFolderName, fileName);

        try (FileWriter writer = new FileWriter(file)){
            writer.write(log);
        }

        git.add().addFilepattern(dateFolderName + "/" + fileName).call();
        git.commit().setMessage("Add new File").call();
        git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""));

        return "https://github.com/XXXXXxi/openai-code-review-log/blob/master" + dateFolderName + "/" + fileName;
    }
}
