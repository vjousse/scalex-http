package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._

import scalex.http._
import format.Formatter

import scalex.search.{ Engine, RawQuery, Results }

import collection.mutable.WeakHashMap
import scalaz.{ Validation, Failure, Success }

object Application extends Controller {

  type Result = Validation[String, String]

  lazy val env = new ScalexHttpEnv
  lazy val engine = env.scalexEnv.engine

  val limit = 15
  val cache = WeakHashMap[RawQuery, Result]()

  def index(q: String, page: Int) = Action { request ⇒
    val query = RawQuery(q, page, limit)
    val result = cache.getOrElseUpdate(query, search(query))
    (result match {
      case Failure(e) ⇒ BadRequest(e)
      case Success(r) ⇒ Ok(r)
    }).as("application/json")

    //getSome("callback") match {
      //case None => contentType = "application/json"; response
      //case Some(c) => contentType = "application/javascript"; "%s(%s)" format (c, response)
    //}
  }

  def search(query: RawQuery): Result = engine find query map {
    case Results(paginator, defs) ⇒ Formatter(query.string, paginator, defs) toString
  }
}
