package generationz.zlog.service.impl;

import generationz.zlog.entity.PublishInput;
import generationz.zlog.service.AnalyzeMultiService;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AnalyzeMultiServiceImpl implements AnalyzeMultiService {
    private final ChatModel chatModel;

    public AnalyzeMultiServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public List<AnalyzeRecord> analyzeAll(PublishInput input) {
        List<AnalyzeRecord> analyzeRecords = new ArrayList<>();
        analyzeRecords.add(analyzeIMG(input.getImgUrl()));
        analyzeRecords.add(analyzeContent(input.getContent()));
        analyzeRecords.add(analyzeTitle(input.getTitle()));
        return analyzeRecords;
    }

    // 拿到分析过后的主题和主要内容
    // 因为 SAA 不支持图片输入，所以图片分析这里依旧是手写 Client 调用openAI API
    @Override
    public AnalyzeRecord analyzeIMG(String imgUrl) {
        String OPENAI_API_URL = "https://api.openai-hub.com/v1/chat/completions";
        String API_KEY = System.getenv("OPENAI_API_KEY");
//        ChatClient imgChatClient = ChatClient.create(OpenAIchatModel);
//        AnalyzeRecord analyzeImg = imgChatClient.prompt()
//                .user("请帮我分析这张照片的主题和主要内容:"+imgURL)
//                .call()
//                .entity(AnalyzeRecord.class);

//        return analyzeImg;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(5000, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .build();
        String base64Image = null;
        try {
            Request imageRequest = new Request.Builder()
                    .url(imgUrl)
                    .build();
            Response imageResponse = client.newCall(imageRequest).execute();

            byte[] imageBytes = imageResponse.body().bytes();
            base64Image = java.util.Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            System.err.println("图片下载或编码失败: " + e.getMessage());
        }
        String jsonBody = String.format("""
            {
                \"model\": \"gpt-4o-2024-05-13\",
                \"messages\": [
                    {
                        \"role\": \"user\",
                        \"content\": [
                            {
                                \"type\": \"text\",
                                \"text\": \"详细描述这张图片的内容，包括主体对象、颜色、场景、氛围和画面感受等方面。描述应该生动形象，不超过50字。\"
                            },
                            {
                                \"type\": \"image_url\",
                                \"image_url\": {
                                    \"url\": \"data:image/jpeg;base64,%s\" 
                                }
                            }
                        ]
                    }
                ]
            }
            """, base64Image);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(OPENAI_API_URL)
                .addHeader("Authorization", API_KEY)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.err.println("OpenAI请求失败: " + response.code() + " - " + response.message());
                return null;
            }
            String responseBody = response.body().string();
            // 只提取 content 字段
            JSONObject json = new JSONObject(responseBody);
            JSONArray choices = json.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            String content = message.getString("content");
            AnalyzeRecord result = new AnalyzeRecord("图片内容", content);
            return result;
        } catch (IOException e) {
            System.err.println("请求出错: " + e.getMessage());
        }
        return null;

    }

    @Override
    public AnalyzeRecord analyzeContent(String content) {
        ChatClient textChatClient = ChatClient.create(chatModel);
        AnalyzeRecord analyzeContent = textChatClient.prompt()
                .user("请帮我分析这段文字内容的主题和主要内容:"+content)
                .call()
                .entity(AnalyzeRecord.class);

        return analyzeContent;
    }

    @Override
    public AnalyzeRecord analyzeTitle(String title) {
        ChatClient titleChatClient = ChatClient.create(chatModel);
        AnalyzeRecord analyzeTilte = titleChatClient.prompt()
                .user("请帮我分析这个小红书标题的主题和主要内容:"+title)
                .call()
                .entity(AnalyzeRecord.class);

        return analyzeTilte;
    }
}
