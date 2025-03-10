package org.example.sdk.domain.service;

import org.example.sdk.infrastructure.git.GitCommand;
import org.example.sdk.infrastructure.openai.IOpenAi;
import org.example.sdk.infrastructure.weixin.WeiXin;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;

public abstract class AbstractOpenAiCodeReviewService implements IOpenAiCodeReviewService {

    private Logger logger = LoggerFactory.getLogger(AbstractOpenAiCodeReviewService.class);

    protected final GitCommand gitCommand;

    protected final IOpenAi openAi;

    protected final WeiXin weiXin;


    protected AbstractOpenAiCodeReviewService(GitCommand gitCommand, IOpenAi openAi, WeiXin weiXin) {
        this.gitCommand = gitCommand;
        this.openAi = openAi;
        this.weiXin = weiXin;
    }

    @Override
    public void exec() {
        try {
            // 1. 获取提交代码
            String diffCode = getDiffCode();
            // 2. 开始评审代码
            String recommend = codeReview(diffCode);
            // 3. 记录评审结果：返回日志地址
            String logUrl = recordCodeReview(recommend);
            // 4. 发送消息通知：日志地址、通知的内容
            pushMessage(logUrl);
        } catch (Exception e) {
            logger.info("openai-code-review error ", e);
        }
    }

    protected abstract String getDiffCode() throws IOException, InterruptedException;

    protected abstract String codeReview(String diffCode) throws Exception;

    protected abstract String recordCodeReview(String recommend) throws Exception;

    protected abstract void pushMessage(String logUrl) throws Exception;
}
