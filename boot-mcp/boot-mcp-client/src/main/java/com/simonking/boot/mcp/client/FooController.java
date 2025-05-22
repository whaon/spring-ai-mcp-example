package com.simonking.boot.mcp.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Spring AI
 *
 * @Author: ws
 * @Date: 2025/2/24 15:10
 */
@RestController
public class FooController {

    @Autowired
    private ChatModel chatModel;
    @Autowired
    private ToolCallbackProvider toolCallbackProvider;

    /**
     * @Description: 普通的调用
     *
     * @Author: ws
     * @Date: 2025/2/24 16:32
     **/
    @GetMapping("/ai/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "推荐一个公众号") String message) {
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultTools(toolCallbackProvider.getToolCallbacks())
                .build();
        CallResponseSpec call = chatClient.prompt(message).call();
        String content = call.content();
        return content;
    }

    @GetMapping(value = "/ai/generate/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generate2(@RequestParam(value = "message", defaultValue = "推荐一个公众号") String message) {
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultTools(toolCallbackProvider.getToolCallbacks())
                .build();
        return chatClient.prompt(message).stream().content();
    }
}
