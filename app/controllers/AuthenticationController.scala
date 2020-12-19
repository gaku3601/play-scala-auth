package controllers

import javax.inject._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, Reads, _}
import play.api.mvc._

import scala.concurrent.ExecutionContext

case class SignUpRequest(email: String, password: String)

@Singleton
class AuthenticationController @Inject()(val controllerComponents: ControllerComponents)(implicit ec: ExecutionContext) extends BaseController {

  implicit val SignUpReads: Reads[SignUpRequest] =
    (JsPath \ "email").read[String](minLength[String](4) keepAnd maxLength[String](30) keepAnd email)
      .and((JsPath \ "password").read[String](minLength[String](4) keepAnd maxLength[String](30)))(SignUpRequest.apply _)

  // This helper parses and validates JSON using the implicit `placeReads`
  // above, returning errors if the parsed json fails validation.
  // TODO: こういう汎用性のあるやつどうするか
  def validateJson[A: Reads]: BodyParser[A] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  def signUp(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def savePlaceConcise: Action[SignUpRequest] = Action(validateJson[SignUpRequest]) { request =>
    // `request.body` contains a fully validated `Place` instance.
    val place = request.body
    println(place)
    Ok(Json.obj("email" -> place.email, "password" -> place.password))
  }
}
