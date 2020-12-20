package controllers

import javax.inject._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, Reads, _}
import play.api.mvc._
import utils.ControllerUtil

import scala.concurrent.ExecutionContext

case class SignUpRequest(email: String, password: String)

object SignUpRequest {
  implicit val SignUpReads: Reads[SignUpRequest] =
    (JsPath \ "email").read[String](minLength[String](4) keepAnd maxLength[String](30) keepAnd email)
      .and((JsPath \ "password").read[String](minLength[String](4) keepAnd maxLength[String](30)))(SignUpRequest.apply _)
}

case class SignUpResponse(email: String)

object SignUpResponse {
  implicit val SignUpWrites: Writes[SignUpResponse] = (res: SignUpResponse) => Json.obj("email" -> res.email)
}

@Singleton
class AuthenticationController @Inject()(val controllerComponents: ControllerComponents, val util: ControllerUtil)(implicit ec: ExecutionContext) extends BaseController {
  def signUp: Action[SignUpRequest] = Action(util.validateJson[SignUpRequest]) { request =>
    val value = request.body
    val response = SignUpResponse(value.email)
    Ok(Json.toJson(response))
  }
}
