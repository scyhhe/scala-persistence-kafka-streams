package jobapi.server.job

import diffson._
import diffson.lcs._
import diffson.playJson._
import diffson.playJson.DiffsonProtocol._
// import diffson.jsonpatch._
import cats.implicits._
import diffson.jsonpatch.lcsdiff._
// import diffson.jsonmergepatch._

import play.api.libs.json.Json
import play.api.libs.json.JsValue
import diffson.jsonpatch.JsonPatch
import scala.util.Try

object State extends App {

  implicit val format = Json.format[JsonPatch[JsValue]]
  implicit val lcs    = new Patience[JsValue]

  val a: JsValue   = Json.obj("a" -> "A")

  val b: JsValue   = Json.obj("a" -> "B")
  
  val ops: JsValue = Json.parse("""{"ops":[{"op":"replace","path":"/a","value":"B"}]}""")
  val op           = ops.as[JsonPatch[JsValue]]

  println(op[Try](a))
  println(Json.toJson(diff(a, b)))

}
