package com.anii.querydsl.gpt.image;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImageRequest implements Serializable {

    /**
     * A text description of the desired image(s). The maximum length is 1000 characters.
     */
    private String prompt;

    /**
     * The number of images to generate. Must be between 1 and 10.
     */
    private Integer n;

    /**
     * The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024.
     */
    private String size;

    /**
     * The format in which the generated images are returned. Must be one of url or b64_json.
     */
    private String responseFormat;

    /**
     * A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
     */
    private String user;
}
