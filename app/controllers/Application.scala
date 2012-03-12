package controllers

import play.api._
import play.api.mvc._

import scalex.http._
import format._

import scalex.search.{ Engine, RawQuery, Results }

import collection.mutable.WeakHashMap
import scalaz.{ Validation, Failure, Success }

object Application extends Controller {

  type Result = Validation[String, String]

  lazy val env = new ScalexHttpEnv
  lazy val engine = env.scalexEnv.engine

  val limit = 15
  val cache = WeakHashMap[RawQuery, Result]()

  def index(q: String, page: Int, callback: String) = Action { request ⇒
    val query = RawQuery(q, page, limit)
    val result = cache.getOrElseUpdate(query, search(query))

    def jsonp(json: String) =
      if (callback.isEmpty) json
      else "%s(%s)" format (callback, json)

    (result match {
      case Failure(e) ⇒ BadRequest(jsonp(errorJson(e)))
      case Success(r) ⇒ Ok(jsonp(r))
    }) as {
      if (callback.isEmpty) "application/json"
      else "application/javascript"
    }
  }

  def errorJson(err: String): String = JsonObject("error" -> err) toString

  def search(query: RawQuery): Result = engine find query map {
    case Results(paginator, defs) ⇒ Formatter(query.string, paginator, defs) toString
  }
}
