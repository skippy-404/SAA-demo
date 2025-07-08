package generationz.zlog.service;

import generationz.zlog.entity.AnalyzeEntity;
import generationz.zlog.entity.PublishInput;
import generationz.zlog.entity.RefineSuggestionEntity;

import java.util.List;

public interface RefineMultiService {
    /**
     * 根据AnalyzeMultiService 的分析结果，进行多模态内容优化
     *
     * @param  analyzeRecords AnalyzeMultiService 的分析结果
     * @return 优化后的多模态内容
     */
    RefineSuggestionEntity refineAll(PublishInput input, List<AnalyzeMultiService.AnalyzeRecord> analyzeRecords);

    /**
     * 根据指定提示词优化图片
     *
     * @param imgURL 图片URL地址
     * @param prompt 优化提示词，指导优化方向
     * @return 根据提示词优化后的图片描述或URL
     */
    String refineIMG(String imgURL,String prompt);

    /**
     * 根据指定提示词优化内容
     *
     * @param content 需要优化的原始内容
     * @param prompt 优化提示词，指导优化方向
     * @return 根据提示词优化后的内容
     */
    String refineContent(String content,String prompt);

    /**
     * 根据指定提示词优化标题
     *
     * @param title 需要优化的原始标题
     * @param prompt 优化提示词，指导优化方向
     * @return 根据提示词优化后的标题
     */
    String refineTitle(String title,String prompt);



}
