package generationz.zlog.service;

import generationz.zlog.entity.PublishInput;

import java.util.List;

public interface AnalyzeMultiService {
    record AnalyzeRecord(String theme, String mainContent) {}

    List<AnalyzeRecord> analyzeAll(PublishInput input);

    /**
     * 分析图片
     * 
     * @param imgUrl 图片URL地址
     * @return 分析照片的主题和内容
     */
//    AnalyzeRecord analyzeIMG(String imgURL);
    AnalyzeRecord analyzeIMG(String imgUrl);
    /**
     * 分析内容
     * 
     * @param content 需要优化的原始内容
     * @return 分析文字的主题和内容
     */
    AnalyzeRecord analyzeContent(String content);

    /**
     * 分析标题
     * 
     * @param title 需要优化的原始标题
     * @return 分析标题的主题和内容
     */
    AnalyzeRecord analyzeTitle(String title);
    


}
