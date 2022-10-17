package com.project.request;

import lombok.Data;

@Data
public class MediaRequest {
    private Long id;
    private String path;
    private String type; // image or video
    private Long questionId;
}
