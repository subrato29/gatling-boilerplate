package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class RampUserLoadSimulation extends Simulation {
  val httpConf = http.proxy(Proxy("localhost", 8888))
    .baseUrl("https://gorest.co.in/")
    .header("Authorization", "Bearer c3146902534dc60968efdab3a95f09cddc07ace1e1708115f6762cc63d844718")

  //ways to read .csv file => circular, shuffle, random, queue
  val csvFeeder = csv("src/test/resources/data/getUser.csv").circular

  def getAllUser() = {
    repeat(1) {
      feed(csvFeeder)
        .exec(http("Get a single user request")
          .get("public-api/users/${userid}")
          .check(jsonPath("$.data.name").is("${name}"))
          .check(status.in(200,304)))
    }
  }

  val scn = scenario("Ramp user load simulation").exec(getAllUser())

  setUp(scn.inject(nothingFor(5),
    atOnceUsers(5),
    constantUsersPerSec(10) during(10 seconds),
    rampUsersPerSec(1) to 5 during(20 seconds)
  ).protocols(httpConf))
}
