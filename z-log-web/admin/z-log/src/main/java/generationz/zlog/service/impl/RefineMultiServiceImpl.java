package generationz.zlog.service.impl;

import generationz.zlog.entity.AnalyzeEntity;
import generationz.zlog.entity.PublishInput;
import generationz.zlog.entity.RefineSuggestionEntity;
import generationz.zlog.service.AnalyzeMultiService;
import generationz.zlog.service.RefineMultiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefineMultiServiceImpl implements RefineMultiService {
    private final ChatModel chatModel;
    public RefineMultiServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public RefineSuggestionEntity refineAll(PublishInput input, List<AnalyzeMultiService.AnalyzeRecord> analyzeRecords) {
        ChatClient RefineAgent = ChatClient.create(chatModel);
        RefineSuggestionEntity refineSuggestion = RefineAgent.prompt("你是一个很懂小红书的小红书达人")
                .user(input+"以上是我要发布的小红书内容\n 这是每一段的大致内容："+analyzeRecords+"请给我标题、正文、配图的修改建议")
                .call()
                .entity(RefineSuggestionEntity.class);
        return refineSuggestion;
    }

    @Override
    public String refineIMG(String imgURL, String prompt) {
        return "";
    }

    @Override
    public String refineContent(String content, String prompt) {

        return "";
    }

    @Override
    public String refineTitle(String title, String prompt) {
        return "";
    }
}
