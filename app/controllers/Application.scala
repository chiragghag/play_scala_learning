package controllers

import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws._
import play.api.Play.current
import play.api.libs.json._
import org.apache.http.HttpStatus
import scala.concurrent._
import akka.util.Timeout
import play.api.libs.functional.syntax._
import org.apache.commons.lang3.Validate
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit._
import scala.util.Success
import scala.util.Try
import scala.util.Failure
//import controllers.WSmock
//import akka.util.duration._
//import play.mvc._

class Application(WStrait: WStrait) extends Controller {

  case class TempAPI(personName: String)

  object TempAPI {
    implicit val jsonFormatter = Json.format[TempAPI]
  }

  /* implicit val rds = (
    (__ \ 'name).read[String]
    ) */
  private val ApiURL = current.configuration.getString("Api.url").get
  private val ContentXML = "Content-Type" -> "application/json"
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  val personForm: Form[testForm] = Form {
    mapping(
      "name" -> text)(testForm.apply)(testForm.unapply)
  }

  /*  object JsonFormats {
  implicit val personFormat = new Format[String] {
    def writes(person: String): JsValue = {
      Json.obj(
        "fName" -> "test"
       )

  }
}}*/

  def testAPI(location: String) = Action.async(parse.json) { request =>
    Logger.info("before validation")
    request.body.validate[TempAPI].map {
      case tempAPI => 
        // var out = new String
        Logger.info("reached the controller" + ApiURL)

        var attempt: Try[Future[WSResponse]] = 
          try{
        	  Success(WStrait.get(ApiURL + location))
          }catch{case ex:Exception =>{
        	  Failure(new RuntimeException("Error Connecting to service(s)"))
          }
          }
        
              attempt match {
        case Success(x) =>{
         mapFuture(x, tempAPI)  
        }
        case Failure(ex) => {
          Future{
             InternalServerError("RestClient Error"+ ex.getMessage())
          }
         }
      }
    
            
    }.recoverTotal(error => Future.successful(BadRequest(JsError.toFlatJson(error))))
  }

  
  private def mapFuture(futuretest: Future[WSResponse], tempAPI: TempAPI) = {
        futuretest.map {
          response =>
            response.status match {
              case 200 => {
                //  try{

                (response.json \ "main" \ "temp").asOpt[Double] match {
                  case None =>
                    InternalServerError("bad status")
                  case parsed => {
                    //   val temp= "%.2f".format(parsed.get).toDouble-273.15
                    Logger.info("before validation after api call-->" + parsed)
                    val temp = parsed.get - 273.15
                    val person = tempAPI.personName
                    Logger.info("after convertig to celsius" + temp)
                    val json: JsObject = Json.obj("greeting" -> (s"Hello $person, the current temperature is $temp Celsius."))
                    Logger.info("after convertig to json" + json)
                    Ok(json)
                  }
                }

              }

              case _ =>
                InternalServerError("bad status")
            }
        }
      }

  
  /* def get(url: String)={
     WS.url(url).withHeaders(ContentXML).get
  }*/

  def sayHello() = Action.async { implicit request =>

    val form = personForm.bindFromRequest.get
    println("inside method" + form.Name)

    val futureresponse = WS.url("http://api.openweathermap.org/data/2.5/weather?q=" + form.Name).withHeaders(ContentXML).get

    futureresponse.map {
      response =>
        Logger.info("i came in " + (response.json \ "main" \ "temp").toString)
        Ok(views.html.index("tempreture in " + form.Name + " is==>" + (response.json \ "main" \ "temp").toString + "K"))
    }
  }
}