package org.example.sdk.infrastructure.openai;

import org.example.sdk.infrastructure.openai.dto.ChatCompletionRequestDTO;
import org.example.sdk.infrastructure.openai.dto.ChatCompletionSyncResponseDTO;

public interface IOpenAi {

    ChatCompletionSyncResponseDTO completions(ChatCompletionRequestDTO request) throws Exception;

}
