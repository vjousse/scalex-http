package scalex.http

import scalex.search.Engine
import scalex.db.{ IndexRepo, DefRepo }

import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.MongoDB

class ScalexHttpEnv {

  lazy val scalexEngine = new Engine(indexRepo.read, defRepo.byIds)

  lazy val indexRepo = new IndexRepo(
    //TODO: load from conf
    "index.dat"
  )

  lazy val defRepo = new DefRepo(
    //TODO: load from conf
    mongoDatabase("scalex")
  )

  private lazy val mongoConnection = MongoConnection(
    //TODO: load from conf
    "localhost",
    27017
  )

  private lazy val mongoDatabase: MongoDB = mongoConnection(
    //TODO: load from conf
    "scalex"
  )
}
