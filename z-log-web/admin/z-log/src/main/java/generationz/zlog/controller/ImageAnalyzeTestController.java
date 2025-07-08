package generationz.zlog.controller;

import generationz.zlog.entity.ResponseEntity;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.model.ChatModel;

@RestController
@RequestMapping("img")
public class ImageAnalyzeTestController {
    private final ChatModel chatModel;
    public ImageAnalyzeTestController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }
    @GetMapping
    public ResponseEntity imgAnalyze(){
        String response = ChatClient.create(chatModel).prompt()
                .user(u -> u.text("先告诉我你是哪个大模型，详细到型号？分析这张照片的内容")
                .media(MimeTypeUtils.IMAGE_JPEG, new ClassPathResource("static/avg.jpg")))
                .call()
                .content();

        return ResponseEntity.success(response);
    }

}
