package io.test

import java.io.Closeable
import java.sql.DriverManager

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import javax.sql.DataSource

case class Person(name:String, age:Int)

object PersonDao {
  import io.getquill._

  val ctx = new PostgresJdbcContext(Literal, "postgresDB")
  import ctx._

  def byName(name:String): List[Person] = {
    ctx.run(query[Person].filter(_.name == "Joe"))
  }
}

object AkkaHttpTest {

  def main(args:Array[String]): Unit = {
    println("Starting")

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route =
      path("hello") {
        get {
          val people = PersonDao.byName("Joe")
          val entity = people.map(p => s"<h2>Say hello to ${p.name}</h2>").mkString("<h1>People:</h1>", "\n", "")
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, entity))
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  }
}
