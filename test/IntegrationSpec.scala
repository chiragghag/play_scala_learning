import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {

  "Application" should {

    "send 404 on a bad path" in new WithApplication{
      route(FakeRequest(GET, "/otherpath")) must beNone
    }

    "render the success response " in new WithApplication{
       val json: JsObject = Json.obj("personName"->"Chirag")
      val home = route(FakeRequest(POST, "/weather/greeter/Mumbai").withBody(json)).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/json")
     // contentAsString(home) must contain ("Your new application is ready.")
    }
    
      "render the response for bad request(400)" in new WithApplication{
       val json: JsObject = Json.obj("personNam"->"Chirag")
      val home = route(FakeRequest(POST, "/weather/greeter/Mumbai").withBody(json)).get

      status(home) must equalTo(400)
      contentType(home) must beSome.which(_ == "application/json")
     // contentAsString(home) must contain ("Your new application is ready.")
    }
  }
}
