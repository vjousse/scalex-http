package scalex.http
package controllers

import play.api._
import play.api.mvc._

import scalex.search.{ Engine, RawQuery, Results }
import org.jboss.netty.handler.codec.http.QueryStringDecoder
import scala.collection.JavaConversions._

import scalaz.{Failure, Success}

object Application extends Controller {

  lazy val env = new ScalexHttpEnv
  lazy val engine = env.scalexEnv.engine

  def index(q: String, page: Int) = Action { request =>
      (engine find RawQuery(q, page, 15) match {
        case Failure(e) => Ok(e)
        case Success(r) => Ok(r.defs.mkString("<br />"))
      }).as("text/html")

  }

}
