package utils

import javax.inject.Inject
import play.api.libs.json.{JsError, Reads}
import play.api.mvc.{BaseController, BodyParser, ControllerComponents}

import scala.concurrent.ExecutionContext

class ControllerUtil @Inject()(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {
  def validateJson[A: Reads]: BodyParser[A] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )
}
