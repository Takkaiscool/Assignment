import com.jayway.restassured.RestAssured;
import com.jayway.restassured.internal.path.json.mapping.JsonObjectDeserializer;
import com.jayway.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

public class ApiTesting {

    @BeforeClass
    public void setup(){
        RestAssured.baseURI="https://jsonplaceholder.typicode.com/";
    }
    @Test
    public void TC001(){

        Response response=RestAssured.given().when().get("posts");
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(response.getContentType(),"application/json; charset=utf-8");
        MatcherAssert.assertThat(response.getBody().jsonPath().getList("$").size(), Matchers.lessThanOrEqualTo(100));
    }

    @Test
    public  void TC002(){
        Response response=RestAssured.given().when().get("posts/1");
        MatcherAssert.assertThat(response.getStatusCode(),CoreMatchers.equalTo(200));
        Assert.assertEquals(response.getContentType(),"application/json; charset=utf-8");
        MatcherAssert.assertThat(response.getBody().jsonPath().getInt("id"),CoreMatchers.equalTo(1));
    }

    @Test
    public void TC003(){
        Response response=RestAssured.given().when().get("invalidposts");
        MatcherAssert.assertThat(response.getStatusCode(),CoreMatchers.equalTo(404));
        RestAssured.given().log().all();
        response.then().log().all();
    }
    @Test
    public void TC004(){
        //Response response=RestAssured.given().when().post("/posts").body();
        JSONObject data=new JSONObject();
        data.put("title","foo");
        data.put("body","bar");
        data.put("userId",1);
        Response response=RestAssured.given().body(data.toString()).post("/posts");
        MatcherAssert.assertThat(response.getStatusCode(),CoreMatchers.equalTo(201));
        Assert.assertEquals(response.getContentType(),"application/json; charset=utf-8");
        Assert.assertEquals(response.getBody().asString(),data.toString());

    }
    @Test
    public void TC005(){
        JSONObject data=new JSONObject();
        data.put("id",1);
        data.put("title","abc");
        data.put("body","bar");
        data.put("userId",1);
        Response response=RestAssured.given().body(data.toString()).put("/posts/1");
        MatcherAssert.assertThat(response.getStatusCode(),CoreMatchers.equalTo(200));
        Assert.assertEquals(response.getContentType(),"application/json; charset=utf-8");
        Assert.assertEquals(response.getBody().asString(),data.toString());
    }
    @Test
    public void TC006(){
        Response response=RestAssured.given().delete("/posts/1");
        MatcherAssert.assertThat(response.getStatusCode(),CoreMatchers.equalTo(200));
        Assert.assertEquals(response.getContentType(),"application/json; charset=utf-8");
    }
}
