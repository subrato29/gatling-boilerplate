package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DataFeederCsv extends Simulation {
  val httpConf = http.baseUrl("https://gorest.co.in/")
    .header("Authorization", "Bearer c3146902534dc60968efdab3a95f09cddc07ace1e1708115f6762cc63d844718")

  //ways to read .csv file => circular, shuffle, random, queue
  val csvFeeder = csv("src/test/resources/data/getUser.csv").circular

  def getAllUser() = {
    repeat(3) {
      feed(csvFeeder)
        .exec(http("Get a single user request")
        .get("public-api/users/${userid}")
        .check(jsonPath("$.data.name").is("${name}"))
        .check(status.in(200,304)))
        .pause(2)
    }
  }

  val scn = scenario("CSV FEEDER TEST").exec(getAllUser())

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)
}
