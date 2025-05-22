package com.example.findy._testutils.docs;

import com.example.findy._core.dto.ApiResponse;
import com.example.findy._core.dto.PageResult;
import com.example.findy._core.dto.SingleResult;
import com.findy.processor.RestDocs;
import com.findy.processor.RestDocsIgnore;
import org.reflections.Reflections;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/docs")
public class DocumentController {

    @GetMapping("/page")
    public ResponseEntity<PageResult<Object>> page() {
        Pageable pageable = PageRequest.of(1, 15, Sort.by("id").descending());
        Page<Object> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        return ApiResponse.ok(emptyPage);
    }

    @GetMapping
    public ResponseEntity<SingleResult<Map<String, Map<String, String>>>> docs() {
        return ApiResponse.ok(getAllEnumClasses());
    }

//    @PostMapping("/upload-file-req")
//    public ResponseEntity<CommonResult> fileUploadUrlReq(@RequestBody FileUploadUrlReq request) {
//        return ApiResponse.ok();
//    }
//
//    @PostMapping("/create-file-info-req")
//    public ResponseEntity<CommonResult> createFileInfoReq(@RequestBody CreateFileInfoReq request) {
//        return ApiResponse.ok();
//    }
//
//    @GetMapping("/file-info-res")
//    public ResponseEntity<SingleResult<FileInfoRes>> fileInfoRes() {
//        return ApiResponse.ok(FileInfoFixture.fileInfoRes(1));
//    }

    @SuppressWarnings("rawtypes")
    private Map<String, Map<String, String>> getAllEnumClasses() {
        Reflections reflections = new Reflections("com.example.findy");
        Set<Class<? extends Enum>> enums = reflections.getSubTypesOf(Enum.class);
        Map<String, Map<String, String>> enumMap = new HashMap<>();

        enums.forEach(enumClass -> {
            if (enumClass.getAnnotation(RestDocsIgnore.class) != null) {
                return;
            }

            Map<String, String> enumValues = new HashMap<>();
            for (Field field : enumClass.getDeclaredFields()) {
                if (field.isEnumConstant()) {
                    RestDocs docs = field.getAnnotation(RestDocs.class);
                    if (docs != null) {
                        String enumValue = field.getName();
                        String description = docs.value();
                        enumValues.put(enumValue, description);
                    }
                }
            }

            enumMap.put(enumClass.getSimpleName(), enumValues);
        });

        return enumMap;
    }

}
