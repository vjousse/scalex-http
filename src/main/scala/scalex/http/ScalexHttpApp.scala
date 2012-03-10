package scalex.http

import com.typesafe.play.mini._

import play.api.mvc.Action
import play.api.mvc.Results._

object ScalexHttpApp extends com.typesafe.play.mini.Application {

  def scalexEnv = new scalex.Env

  def route = {

    case GET(Path("/")) => Action {
      Ok(<a href="https://github.com/ornicar/scalex/blob/master/http-api-documentation.md">Documentation</a>).as("text/html")
    }

    case GET(Path("/")) & QueryString(qs) => Action {
      Ok(<h1>It works!</h1>).as("text/html")
    }
  }
}
