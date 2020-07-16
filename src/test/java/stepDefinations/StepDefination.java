package stepDefinations;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.location;
import pojo.mapLocation;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;


public class StepDefination extends Utils{
	RequestSpecification  res;
	ResponseSpecification respec;
	Response response;
	TestDataBuild data = new TestDataBuild();
	static String place_id;
	@Given("Add Place Playload with {string} {string} {string}")
	
	public void add_place_playload_with(String name, String language, String address) throws IOException {
    // Write code here that turns the phrase above into concrete actions
			respec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
			res = given().spec(requestSpecification()).body(data.addPlacePayload(name,language,address));
		
		}



	@When("user calls {string} with {string} http request")
	public void user_calls_with_http_request(String resource, String method) {
		APIResources resourceAPI=APIResources.valueOf(resource);
			System.out.println(resourceAPI.getResource());
			
			if (method.equalsIgnoreCase("POST"))
			{
				response = res.when().post(resourceAPI.getResource());
						
			}
			else if (method.equalsIgnoreCase("get"))
			{
				response = res.when().get(resourceAPI.getResource());
			}
				
		}
		@Then("the API call got sucess with status code {int}")
		public void the_api_call_got_sucess_with_status_code(Integer int1) {
		  assertEquals(response.getStatusCode(),200);
			
		}
		
		@Then("{string} in response body is {string}")
		public void in_response_body_is(String keyValue, String Expectedvalue) {
		
		assertEquals(getJsonPath(response,keyValue),Expectedvalue);
		
		}
		@Then("verify place_id created maps to {string} using {string}")
		public void verify_place_id_created_maps_to_using(String expectedName, String resource) throws IOException {
			 place_id =getJsonPath(response,"place_id");
			res = given().spec(requestSpecification()).queryParam("place_id",place_id);
			user_calls_with_http_request(resource,"GET");
			String actualName =getJsonPath(response,"name");
			assertEquals(actualName,expectedName);
			
		   
		}


		@Given("DeletePlace Payload")
		public void delete_place_payload() throws IOException {
		    // Write code here that turns the phrase above into concrete actions
		  res= given().spec(requestSpecification()).body(data.deletePlacePayload(place_id));
		}




}
