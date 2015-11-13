package controllers

import play.api.libs.ws._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current
import scala.concurrent.Future
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit._
import scala.concurrent._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api._
trait WStrait {
  
    def get(url: String):Future[WSResponse]

}

object WSmock extends WStrait{
  
    private val ContentXML = "Content-Type" -> "application/json"
  
  def get(url: String):Future[WSResponse]={
      Logger.info("inside get method")
   WS.url(url).withHeaders(ContentXML).get
   
  
  }
}
