package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TestApi {
    private final String getResourceList = "unknown";//"/{resource}";
    private final String postRegisterUser = "/register";
    private final String updateUser = "/users/2";
    private final String deleteUser = "/users/2";
    private final String postLoginUser = "/login";
    private final String userName = "eve.holt@reqres.in";
    private final String userEmail = "eve.holt@reqres.in";
    private final String userPassword = "pistol";
    public TestApi() {
    }

    @Test
    public void testGetListResource(){
        List<ResourceObject> names = given()
                .spec(BaseApiTest.getBaseSpecification())
                .when()
                .get(getResourceList)
                .then()
                .spec(BaseApiTest.getBaseResponse200Specification())
                .extract()
                .jsonPath()
                .getList("data", ResourceObject.class);

        Assertions.assertTrue(names.stream().allMatch(e->e.getName().length()>0));
    }
    @Test
    public void testPostRegisterUser(){
        RegisterUser myUser = new RegisterUser(userName, userEmail, userPassword);
        RegisterUserResponse myResponse =
                 given()
                .spec(BaseApiTest.getBaseSpecification())
                         .when()
                         .body(myUser)
                         .post(postRegisterUser)
                         .then()
                         .spec(BaseApiTest.getBaseResponse200Specification())
                         .extract()
                         .jsonPath()
                         .getObject("",RegisterUserResponse.class)
                         //.body("id", Matchers.equalTo(4))
                         //.body("token",Matchers.equalTo("QpwL5tke4Pnpja7X4"))
                         ;
        Assertions.assertTrue((myResponse.getId() > 0) && (myResponse.getToken().length()>0));
    }
    @Test
    public void testUpdateUser(){
        String isoDatePattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(isoDatePattern);
        String dateString = simpleDateFormat.format(new Date());

        given()
                .spec(BaseApiTest.getBaseSpecification())
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .put(updateUser)
                .then()
                .spec(BaseApiTest.getBaseResponse200Specification())
                .body("name", Matchers.equalTo("morpheus"))
                .body("job", Matchers.equalTo("zion resident"))
                .body("updatedAt", Matchers.containsString(dateString.substring(0,11)))
        ;
    }
    @Test
    public void testDeleteUser(){
        given()
                .spec(BaseApiTest.getBaseSpecification())
                .when()
                .delete(deleteUser)
                .then()
                .spec(BaseApiTest.getBaseResponse204Specification())
                ;
    }
    @Test
    public void testPostLoginUser() throws IOException {
        File loginUser = new File("src/test/java/org/example/login_body.json");
        RegisterUser myUser = new ObjectMapper().readValue(loginUser, RegisterUser.class);
        given()
                .spec(BaseApiTest.getBaseSpecification())
                .when()
                .body(myUser)
                .post(postLoginUser)
                .then()
                .spec(BaseApiTest.getBaseResponse200Specification())
                .body("token", Matchers.not(Matchers.emptyString()))
                ;
    }
}
