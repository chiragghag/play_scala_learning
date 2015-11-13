package controllers
import play.api.libs.json.Json

case class testForm(Name: String) 

  
  object testForm{
    
    implicit val testFormFormat=Json.format[testForm]
  }

