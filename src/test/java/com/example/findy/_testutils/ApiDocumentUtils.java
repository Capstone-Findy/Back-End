package com.example.findy._testutils;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.example.findy._testutils.docs.DocumentLinkGenerator;
import org.springframework.data.domain.Pageable;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.*;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.PathParametersSnippet;
import org.springframework.restdocs.request.QueryParametersSnippet;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.stream.Stream;

public interface ApiDocumentUtils {
    static ParameterDescriptor[] pageableParameters() {
        return new ParameterDescriptor[]{
                parameterWithName("page").description("페이지 번호"),
                parameterWithName("size").description("페이지 크기"),
                parameterWithName("sort").description("정렬 (정렬 필드는 Response fields-data key 값 중 하나) ").optional()
        };
    }

    private static FieldDescriptor[] commonResultFields() {
        return new FieldDescriptor[]{
                fieldWithPath("error").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("메세지").optional(),
        };
    }

    private static FieldDescriptor[] singleResultFields() {
        return Stream.concat(
                Stream.of(commonResultFields()),
                Stream.of(subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"))
        ).toArray(FieldDescriptor[]::new);
    }

    private static FieldDescriptor[] enumResultFields(Class<? extends Enum<?>> enumClass) {
        return new FieldDescriptor[]{
                fieldWithPath("error").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("메세지").optional(),
                fieldWithPath("data").description(DocumentLinkGenerator.generateLinkCode(enumClass))
        };
    }

    private static FieldDescriptor[] listResultFields() {
        return Stream.concat(
                Stream.of(commonResultFields()),
                Stream.of(subsectionWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"))
        ).toArray(FieldDescriptor[]::new);
    }

    private static FieldDescriptor[] listResultFields(String description) {
        return Stream.concat(
                Stream.of(commonResultFields()),
                Stream.of(subsectionWithPath("data").type(JsonFieldType.ARRAY).description(description))
        ).toArray(FieldDescriptor[]::new);
    }

    private static FieldDescriptor[] pageResultFields() {
        return Stream.concat(
                Stream.of(commonResultFields()),
                Stream.of(
                        subsectionWithPath("data").type(JsonFieldType.ARRAY).description("응답 데이터"),
                        subsectionWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보")
                )
        ).toArray(FieldDescriptor[]::new);
    }

    static RestDocumentationResultHandler document(
            String identifier,
            Snippet snippet,
            Snippet[] snippets
    ) {
        Snippet[] combined = Stream.concat(
                Stream.of(snippet),
                Stream.of(snippets)
        ).toArray(Snippet[]::new);

        return document(
                identifier,
                combined
        );
    }

    static RestDocumentationResultHandler document(
            String identifier,
            Snippet[] snippets1,
            Snippet[] snippets2
    ) {
        Snippet[] combined = Stream.concat(
                Stream.of(snippets1),
                Stream.of(snippets2)
        ).toArray(Snippet[]::new);

        return document(
                identifier,
                combined
        );
    }

    static RestDocumentationResultHandler document(
            String identifier,
            Snippet snippet1,
            Snippet snippet2,
            Snippet[] snippets
    ) {
        Snippet[] combined = Stream.concat(
                Stream.concat(Stream.of(snippet1), Stream.of(snippet2)),
                Stream.of(snippets)
        ).toArray(Snippet[]::new);

        return document(
                identifier,
                combined
        );
    }

    static RestDocumentationResultHandler document(
            String identifier,
            Snippet snippet1,
            Snippet snippet2,
            Snippet snippet3,
            Snippet[] snippets
    ) {
        Snippet[] combined = Stream.concat(
                Stream.concat(Stream.concat(Stream.of(snippet1), Stream.of(snippet2)), Stream.of(snippet3)),
                Stream.of(snippets)
        ).toArray(Snippet[]::new);

        return document(
                identifier,
                combined
        );
    }

    static RestDocumentationResultHandler document(
            String identifier,
            Snippet... snippets
    ) {
        return MockMvcRestDocumentationWrapper.document(
                identifier,
                snippets
        );
    }

    static MultiValueMap<String, String> pageableParams(Pageable pageable) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("page", String.valueOf(pageable.getPageNumber()));
        parameters.add("size", String.valueOf(pageable.getPageSize()));
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                String sortParam = order.getProperty() + "," + order.getDirection().name().toLowerCase();
                parameters.add("sort", sortParam);
            });
        }
        return parameters;
    }

    static Snippet queryParametersWithPageable(ParameterDescriptor... descriptors) {
        ParameterDescriptor[] parameterDescriptors = pageableParameters();
        return queryParameters(
                Stream.concat(
                        Stream.of(parameterDescriptors),
                        Stream.of(descriptors)
                ).toArray(ParameterDescriptor[]::new)
        );
    }

    static Snippet[] responseFieldsForCommonResult() {
        return Stream.of(PayloadDocumentation.responseFields(commonResultFields())).toArray(Snippet[]::new);
    }

    static Snippet[] responseFieldsForSingleResult(FieldDescriptor... descriptors) {
        FieldDescriptor[] responseFields = singleResultFields();
        Snippet contentSnippet = PayloadDocumentation.responseFields(
                PayloadDocumentation.beneathPath("data").withSubsectionId("data"),
                descriptors
        );

        return Stream.concat(
                Stream.of(PayloadDocumentation.responseFields(responseFields)),
                Stream.of(contentSnippet)
        ).toArray(Snippet[]::new);
    }

    static Snippet[] responseFieldsForEnumListResult(Class<? extends Enum<?>> enumClass) {
        return Stream.of(PayloadDocumentation.responseFields(enumResultFields(enumClass))).toArray(Snippet[]::new);
    }

    static Snippet[] responseFieldsForListResult(String description) {
        FieldDescriptor[] responseFields = listResultFields(description);
        return new Snippet[]{PayloadDocumentation.responseFields(responseFields)};
    }

    static Snippet[] responseFieldsForListResult(FieldDescriptor... descriptors) {
        FieldDescriptor[] responseFields = listResultFields();
        Snippet contentSnippet = PayloadDocumentation.responseFields(
                PayloadDocumentation.beneathPath("data[]").withSubsectionId("data"),
                descriptors
        );

        return Stream.concat(
                Stream.of(PayloadDocumentation.responseFields(responseFields)),
                Stream.of(contentSnippet)
        ).toArray(Snippet[]::new);
    }

    static Snippet[] responseFieldsForPageResult(FieldDescriptor... descriptors) {
        FieldDescriptor[] responseFields = pageResultFields();
        ResponseFieldsSnippet contentSnippet = PayloadDocumentation.responseFields(
                PayloadDocumentation.beneathPath("data[]").withSubsectionId("data"),
                descriptors
        );
        ResponseFieldsSnippet pageSnippet = PayloadDocumentation.responseFields(
                PayloadDocumentation.beneathPath("pageInfo").withSubsectionId("pageInfo"),
                fieldWithPath("totalElements").type(JsonFieldType.NUMBER).description("전체 개수"),
                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                fieldWithPath("page").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
                fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 크기"),
                fieldWithPath("hasPrevious").type(JsonFieldType.BOOLEAN).description("이전 페이지"),
                fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지")
        );

        return Stream.concat(
                Stream.of(PayloadDocumentation.responseFields(responseFields)),
                Stream.concat(
                        Stream.of(contentSnippet),
                        Stream.of(pageSnippet)
                )
        ).toArray(Snippet[]::new);
    }

    static ParameterDescriptor parameterWithName(String name) {
        return RequestDocumentation.parameterWithName(name);
    }

    static QueryParametersSnippet queryParameters(ParameterDescriptor... descriptors) {
        return RequestDocumentation.queryParameters(descriptors);
    }

    static PathParametersSnippet pathParameters(ParameterDescriptor... descriptors) {
        return RequestDocumentation.pathParameters(descriptors);
    }

    static RequestFieldsSnippet requestFields(FieldDescriptor... descriptors) {
        return PayloadDocumentation.requestFields(descriptors);
    }

    static MockHttpServletRequestBuilder get(String urlTemplate, Object... urlVariables) {
        return RestDocumentationRequestBuilders.get(urlTemplate, urlVariables);
    }

    static MockHttpServletRequestBuilder post(String urlTemplate, Object... urlVariables) {
        return RestDocumentationRequestBuilders.post(urlTemplate, urlVariables);
    }

    static MockHttpServletRequestBuilder put(String urlTemplate, Object... urlVariables) {
        return RestDocumentationRequestBuilders.put(urlTemplate, urlVariables);
    }

    static MockHttpServletRequestBuilder delete(String urlTemplate, Object... urlVariables) {
        return RestDocumentationRequestBuilders.delete(urlTemplate, urlVariables);
    }

    static MockHttpServletRequestBuilder patch(String urlTemplate, Object... urlVariables) {
        return RestDocumentationRequestBuilders.patch(urlTemplate, urlVariables);
    }

    static StatusResultMatchers status() {
        return MockMvcResultMatchers.status();
    }

    static FieldDescriptor fieldWithPath(String path) {
        return PayloadDocumentation.fieldWithPath(path);
    }

    static SubsectionDescriptor subsectionWithPath(String path) {
        return PayloadDocumentation.subsectionWithPath(path);
    }

}
