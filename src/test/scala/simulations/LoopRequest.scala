package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class LoopRequest extends Simulation {
  val httpConf = http.baseUrl(url = "https://reqres.in/")
    .header(name = "Accept", value = "application/json")
    .header(name = "content-type", value = "application/json")

  def getAllUsersRequest() = {
    repeat(2) {
      exec(http("Get all users request")
        .get("api/users?page=2")
        .check(status.is(200))
      )
    }
  }

  def getSingleUsersRequest() = {
    repeat(2) {
      exec(http("Get single user request")
        .get("api/users/2")
        .check(status.is(200))
      )
    }
  }

  def addUser() = {
    repeat(2) {
      exec(http("Add a user request")
        .post("api/users")
        .body(RawFileBody("./src/test/resources/bodies/AddUser.json")).asJson
        .check(status.is(201)))
    }
  }

  val scn = scenario("User request scenario")
    .exec(getAllUsersRequest())
    .pause(2)
    .exec(getSingleUsersRequest())
    .pause(2)
    .exec(addUser())

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)
}
