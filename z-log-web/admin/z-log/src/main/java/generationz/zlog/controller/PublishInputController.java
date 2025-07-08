package generationz.zlog.controller;

import generationz.zlog.entity.RefineSuggestionEntity;
import generationz.zlog.entity.ResponseEntity;
import generationz.zlog.entity.PublishInput;
import generationz.zlog.service.AnalyzeMultiService;
import generationz.zlog.service.RefineMultiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/publish")
@Tag(name = "内容发布与优化 API", description = "提供内容分析和优化建议的功能")
public class PublishInputController {

    @Autowired
    AnalyzeMultiService analyzeMultiService;
    @Autowired
    RefineMultiService refineMultiService;

    @Operation(summary = "分析所有输入内容", description = "一次性分析图片、内容和标题，返回各自的分析结果。")
    @GetMapping("/analyze")
    public List<AnalyzeMultiService.AnalyzeRecord> analyzeAll(@RequestBody PublishInput input){
        return analyzeMultiService.analyzeAll(input);
    }

    @Operation(summary = "获取所有内容的优化建议", description = "基于分析结果，为图片、内容和标题提供统一的优化建议。")
    @GetMapping("/refine")
    public ResponseEntity refineAll(@RequestBody PublishInput input){
        List<AnalyzeMultiService.AnalyzeRecord> analyzeRecords= new ArrayList<>();
        analyzeRecords = analyzeAll(input);
        RefineSuggestionEntity result = refineMultiService.refineAll(input,analyzeRecords);
        return ResponseEntity.success(result);
    }

    @Operation(summary = "分析图片", description = "单独分析给定的图片URL，并返回分析结果。")
    @GetMapping("/img")
    public ResponseEntity analyzeIMG(@RequestBody PublishInput input){
        String imgUrl = input.getImgUrl();
        AnalyzeMultiService.AnalyzeRecord result = analyzeMultiService.analyzeIMG(imgUrl);
        return ResponseEntity.success(result);
    }

    @Operation(summary = "分析文本内容", description = "单独分析给定的文本内容，并返回分析结果。")
    @GetMapping("/content")
    public ResponseEntity analyzeContent(@RequestBody PublishInput input){
        String content = input.getContent();
        AnalyzeMultiService.AnalyzeRecord result = analyzeMultiService.analyzeContent(content);
        return ResponseEntity.success(result);
    }

    @Operation(summary = "分析标题", description = "单独分析给定的标题，并返回分析结果。")
    @GetMapping("/title")
    public ResponseEntity analyzeTitle(@RequestBody PublishInput input){
        String title = input.getTitle();
        AnalyzeMultiService.AnalyzeRecord result = analyzeMultiService.analyzeTitle(title);
        return ResponseEntity.success(result);
    }
}
