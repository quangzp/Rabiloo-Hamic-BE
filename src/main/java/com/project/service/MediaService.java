package com.project.service;

import com.project.request.MediaRequest;
import com.project.response.MediaResponse;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService extends BaseService<MediaResponse,MediaRequest> {
    MediaResponse createNew(MultipartFile file);
}
