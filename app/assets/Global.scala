import init.ProvisioningOrchestratorInitializer
import play.api.{Logger, Application}
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.Future

object Global extends ProvisioningOrchestratorInitializer {

  val log: Logger = Logger(this.getClass)

  override def onHandlerNotFound(request: RequestHeader) = {
    Future.successful(NotFound("Not Found"))
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful(BadRequest("Bad Request: " + error))
  }
}