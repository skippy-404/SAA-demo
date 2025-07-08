package generationz.zlog.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class chatClientConfig {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("你是一个很懂小红书的AI")
                .build();
    }

}

