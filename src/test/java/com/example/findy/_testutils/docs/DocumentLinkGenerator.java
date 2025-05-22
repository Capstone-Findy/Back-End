package com.example.findy._testutils.docs;


import com.findy.processor.RestDocs;

public interface DocumentLinkGenerator {
    static String generateLinkCode(Class<? extends Enum<?>> enumClass) {
        RestDocs restDocs = enumClass.getAnnotation(RestDocs.class);
        String description = restDocs.value();

        return String.format(
                "link:docs/enum/%s.html[%s %s,role='popup',window='_blank']",
                enumClass.getSimpleName(),
                description,
                "코드"
        );
    }

//    static String generateFileUploadUrlReqLinkCode() {
//        return String.format(
//                "link:common/%s.html[%s,role='popup',window='_blank']",
//                FileUploadUrlReq.class.getSimpleName(),
//                "파일 업로드"
//        );
//    }
//
//    static String generateCreateFileInfoReqLinkCode(String description) {
//        return String.format(
//                "link:common/%s.html[%s,role='popup',window='_blank']",
//                CreateFileInfoReq.class.getSimpleName(),
//                description
//        );
//    }
//
//    static String generateFileInfoResLinkCode(String description) {
//        return String.format(
//                "link:common/%s.html[%s,role='popup',window='_blank']",
//                FileInfoRes.class.getSimpleName(),
//                description
//        );
//    }
}
