package com.asem.ucar.Image.сontroller;

import com.asem.ucar.Image.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")
@RequiredArgsConstructor
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @GetMapping("/signature")
    public Map<String, Object> getSignature(@RequestParam(required = false) String folder) {
        return cloudinaryService.getDirectUploadSignature(folder);
    }
}