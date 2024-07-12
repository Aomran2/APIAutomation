package tests;


import io.restassured.http.ContentType;
import org.testng.annotations.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredAutomation {



    //Set the base URL for the API before any test is run
    @BeforeTest
    public void BaseURL(){
        RestAssured.baseURI = "https://reqres.in/";
    }


    //Verify the status code and some specific fields in the response body
    @Test
    public void VerifyStatusCode (){

        given().log().all().
                when().
                get("api/users").
                then().log().all().assertThat().statusCode(200)
                .assertThat().body("page", equalTo(1))
                .assertThat().body("per_page", equalTo(6))
                .assertThat().body("total", equalTo(12))
                .assertThat().body("total_pages", equalTo(2));
    }


    //Verify the number of users in the response body
    @Test
    public void verifyUsersCount(){

        Response response = given().
                when().log().all()
                .get("api/users").
                then().log().all().statusCode(200)
                .extract().response();

        List <Integer> users = response.jsonPath().getList("data");
        int numberOfUsers = users.size();
        assertThat(numberOfUsers, equalTo(6));
    }


    //Data provider that provides test data to the test method
    @DataProvider (name = "useGroupOfData")
    public static Object [] [] groupOfUsers(){
        return new Object[][]{
                {0,1,"george.bluth@reqres.in","George","Bluth"},
                {1,2,"janet.weaver@reqres.in","Janet","Weaver"},
                {2,3,"emma.wong@reqres.in","Emma","Wong"},
                {3,4,"eve.holt@reqres.in","Eve","Holt"},
                {4,5,"charles.morris@reqres.in","Charles","Morris"},
                {5,6,"tracey.ramos@reqres.in","Tracey","Ramos"},
        };
    }



    //Verify specific user details using parameters provided by the data provider
    @Test (dataProvider = "useGroupOfData")
    public void verifyFirstUser(int index, int id, String email, String firstName, String lastName){

        given().log().all()
                .when()
                .get("api/users")
                .then().log().all().statusCode(200)
                .assertThat().body("data["+ index +"].id", equalTo(id))
                .assertThat().body("data[ "+ index + "].email", equalTo(email))
                .assertThat().body("data[ "+ index +"].first_name", equalTo(firstName))
                .assertThat().body("data["+ index +"].last_name", equalTo(lastName));
    }


    //Create single user
    @Test
    public void createSingleUser() {

        String requestBody = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";

        given().log().all()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().post("api/users")
                .then().log().all().statusCode(201)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("leader"));

    }


    //updating an existing single user
    @Test
    public void updateSingleUser() {

        String requestBody = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";

        given().log().all()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().put("api/users/2")
                .then().log().all().statusCode(200)
                .body("name", equalTo("morpheus"))
                .body("job", equalTo("zion resident"));

    }


    //Deleting an existing single user
    @Test
    public void deleteSingleUser() {

        String requestBody = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";

        given().log().all()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().delete("api/users/2")
                .then().log().all().statusCode(204);

    }


    //Fail test case, status code should be 200
    @Test
    public void unableToLogin() {

        String requestBody = "{\n" +
                "    \"email\": \"eve.holt@reqres.in\",\n" +
                "    \"password\": \"cityslicka\"\n" +
                "}";

        given().log().all()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().post("api/login")
                .then().log().all().statusCode(401);
    }

}
