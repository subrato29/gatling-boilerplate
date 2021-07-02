package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CheckResponseAndExtract extends Simulation {
  val httpConf = http.baseUrl("https://gorest.co.in/")
    .header("Authorization", "Bearer c3146902534dc60968efdab3a95f09cddc07ace1e1708115f6762cc63d844718")

  val scn = scenario("Check correlation and extract data")

    //first call- get all the users
    .exec(http("Get all users")
      .get("public-api/users")
      .check(jsonPath("$.data[0].id").saveAs("userId")))

    //second api- get a specific user on the basis of id
    .exec(http("Get specific user")
      .get("public-api/users/${userId}")
      .check(jsonPath("$.data.id").is("1"))
      .check(jsonPath("$.data.name").is("Dr. Anusuya Gowda"))
      .check(jsonPath("$.data.email").is("dr_anusuya_gowda@berge-greenfelder.biz")))

    setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)
}
