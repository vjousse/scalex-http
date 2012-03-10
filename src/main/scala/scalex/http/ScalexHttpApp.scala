package scalex
package http

import scalex.search.{ Engine, RawQuery, Results }

import com.typesafe.play.mini._

import play.api.mvc.Action
import play.api.mvc.Results._

import org.jboss.netty.handler.codec.http.QueryStringDecoder
import scala.collection.JavaConversions._

import scalaz._

object ScalexHttpApp extends com.typesafe.play.mini.Application {

  val env = new ScalexHttpEnv
  val scalexEngine = env.scalexEngine

  def route = {

    //Do the actual search
    case GET(Path("/")) & QueryString(qs) => Action {
      (scalexEngine find RawQuery(qs, page(new QueryStringDecoder("?" + qs)), 15) match {
        case Failure(e) => Ok(e)
        case Success(r) => Ok(r.defs.mkString("<br />"))
      }).as("text/html")
    }

    case GET(Path("/")) => Action {
      Ok(<a href="https://github.com/ornicar/scalex/blob/master/http-api-documentation.md">Documentation</a>).as("text/html")
    }

  }

  def page(decoder: QueryStringDecoder): Int = getSome("page", decoder) map (_.toInt) getOrElse (1)

  def getSome(param: String, decoder: QueryStringDecoder): Option[String] = decoder.getParameters().get(param).headOption
}
