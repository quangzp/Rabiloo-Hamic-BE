package com.project.controller;

import com.project.response.MediaResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
public class MediaController {

    //TODO config and add api create, update, delete media


    @PostMapping("/admin/image-upload")
    public MediaResponse handleFileUpload(@RequestParam("file") MultipartFile file) {
        /*MediaResponse response = new MediaResponse();
        try {
            UUID uuid = UUID.randomUUID();
            storageService.store(file, uuid);

            MediaDto dto = new MediaDto();
            dto.setPath(uuid + "-" + file.getOriginalFilename());
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage("Error + " + e.getMessage());
            response.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        return response;*/

        return null;
    }
}
