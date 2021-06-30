package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UpdateUserSimulation extends Simulation {
  val httpConf = http.baseUrl("https://reqres.in/")
    .header("Accept", "application/json")
    .header("content-type", "application/json")

  val scn = scenario("Update user scenario")

    //first- updating the user
    .exec(http("Update specific user")
      .put("api/user/2")
      .body(RawFileBody("src/test/resources/bodies/UpdateUser.json")).asJson
      .check(status.in(200 to 201)))

    .pause(3)

    //second- deleting the user
    .exec(http("Delete user")
      .delete("api/user/2")
      .check(status.is(204)))

    setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)
}
