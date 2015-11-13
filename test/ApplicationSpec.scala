import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json._
import play.api.test._
import akka.util._
import play.api.test.Helpers._
//mockitto
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.mockito.Mockito._
import org.mockito.Matchers._
import controllers._
import controllers.Application
import scala.concurrent._
import play.api.libs.ws._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification with Mockito {

  "Application for success response" in {
    running(FakeApplication()) {
      val mockUserDALObject = mock[WStrait]
      val userController = new Application(mockUserDALObject)
      val Apiresponse: JsObject = Json.obj("main" -> Json.obj("temp" -> 282.41))

      val wsResponse: play.api.libs.ws.WSResponse = mock[play.api.libs.ws.WSResponse]
      wsResponse.status returns 200
      wsResponse.body returns "{\"main\":{\"temp\":282.41}}"
      wsResponse.json returns Apiresponse
      val futureResponsesuccess = scala.concurrent.Future { wsResponse }

      when(mockUserDALObject.get(any[String])).thenReturn(futureResponsesuccess)

      //   "respond to the index Action" in {
      val mainjson: JsObject = Json.obj("personName" -> "Chirag")
      val result = userController.testAPI("london")(FakeRequest().withBody(mainjson).withHeaders("Content-Type" -> "application/json"))

      status(result) must equalTo(200)
      contentType(result) must beSome("application/json")
      contentAsString(result) must contain("greeting")

    }
  }
    "Application for epmty response" in {
    running(FakeApplication()) {
      val mockUserDALObject = mock[WStrait]
      val userController = new Application(mockUserDALObject)
      val Apiresponse = JsNull

      val wsResponse: play.api.libs.ws.WSResponse = mock[play.api.libs.ws.WSResponse]
      wsResponse.status returns 200
      wsResponse.body returns "{}"
      wsResponse.json returns Apiresponse
      val futureResponsefailed = scala.concurrent.Future { wsResponse }

      when(mockUserDALObject.get(any[String])).thenReturn(futureResponsefailed)

      //   "respond to the index Action" in {
      val mainjson: JsObject = Json.obj("personName" -> "Chirag")
      val result = userController.testAPI("london")(FakeRequest().withBody(mainjson).withHeaders("Content-Type" -> "application/json"))

      status(result) must equalTo(500)
    //  contentType(result) must beSome("application/json")
      contentAsString(result) must not be empty

    }
    }
    "Application for no connection" in {
    running(FakeApplication()) {
      val mockUserDALObject = mock[WStrait]
      val userController = new Application(mockUserDALObject)

      when(mockUserDALObject.get(any[String])).thenThrow(new RuntimeException("Resource not found"))

      //   "respond to the index Action" in {
      val mainjson: JsObject = Json.obj("personName" -> "Chirag")
      
      val result = userController.testAPI("london")(FakeRequest().withBody(mainjson).withHeaders("Content-Type" -> "application/json"))

      status(result) must equalTo(500)
    //  contentType(result) must beSome("application/json")
      contentAsString(result) must not be empty

    }
  }
}
