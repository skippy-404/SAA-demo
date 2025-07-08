package generationz.zlog.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户发布内容的输入实体")
public class PublishInput {

    @Schema(description = "帖子标题", example = "我的春日VLOG")
    private String title;

    @Schema(description = "帖子正文内容", example = "今天天气真好，出门踏青...")
    private String content;

    @Schema(description = "图片URL地址", example = "https://example.com/image.jpg")
    private String imgUrl;

}
