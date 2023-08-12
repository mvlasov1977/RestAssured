package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class BaseApiTest {
    private static final String startURL = "https://reqres.in/api";

    public BaseApiTest() {
    }

    public static RequestSpecification getBaseSpecification(){
        return new RequestSpecBuilder()
                .setBaseUri(startURL)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
    public static ResponseSpecification getBaseResponse200Specification(){
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();
    }
    public static ResponseSpecification getBaseResponse204Specification(){
        return new ResponseSpecBuilder()
                .expectStatusCode(204)
                .build();
    }
}
