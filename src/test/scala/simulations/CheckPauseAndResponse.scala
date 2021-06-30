package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt

class CheckPauseAndResponse extends Simulation{
  val httpConf = http.baseUrl(url = "https://reqres.in/")
    .header("Accept", "application/json")

  val scn = scenario("User API calls")

    .exec(http("List all users")
      .get("/api/users?page=2")
      .check(status.is(200)))
      .pause(5)

    .exec(http("Single user API")
      .get("api/users/2")
      .check(status.in(200 to 210)))
      .pause(1, 10)

    .exec(http("Single user not found API")
      .get("api/users/23")
      .check(status.not(400), status.not(500)))
      .pause(3000.milliseconds)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)
}
