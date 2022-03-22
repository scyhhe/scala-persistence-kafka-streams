package jobapi.api.model

import diffson.jsonpatch._
import diffson.jsonpointer.Pointer
import diffson.playJson._
import play.api.libs.json._

object JsonPatchExample {
  val value = JsonPatch[JsValue](
    List(
      Add[JsValue](
        Pointer("contact", "email"),
        JsString("eagle.hunter@firstbird.com")
      ),
      Remove(
        Pointer("responsible") / 0
      )
    )
  )
}
