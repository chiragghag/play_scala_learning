package init

//import actors.ProvisioningOrchestrator
import akka.actor.{Props, ActorRef}
import controllers.Application
import play.api.libs.concurrent.Akka
import play.api.{Logger, GlobalSettings}
import controllers.WStrait

/** Creates provisioning orchestrator actor on application start-up */
trait ProvisioningOrchestratorInitializer extends GlobalSettings {

  override def getControllerInstance[A](controllerClass: Class[A]) = {
    if (controllerClass != classOf[Application]) {
      throw new RuntimeException("Could not create a controller of type" + controllerClass)
    }
    new Application(controllers.WSmock).asInstanceOf[A]
  }
}
