## ProjetoModelo - Testes de Performance/Carga com Gatling

Projeto com base no modelo "quick start bundle" para testes com <a href="https://github.com/gatling/gatling.g8">Gatling</a>.

[sbtplugindoc]: https://gatling.io/docs/current/extensions/sbt_plugin/
[gatlingdoc]: https://gatling.io/docs/current/advanced_tutorial/



### Criar _Simulation_

Para simplificar a criação dos testes, foi criado um <a href="https://docs.scala-lang.org/tour/traits.html">trait</a> [SpecTrait], 
que define por meio de um objeto ```Specs``` um conjunto básico de valores que podem ser dispostos
em um arquivo de configuração '.conf'.
SpecTrait possui um objeto ```Sepcs``` definido por meio de uma função anônima.

O arquivo de configuração possui o seguinte formato:

```
specs {

  headers = {"Content-Type": "application/x-www-form-urlencoded", "client_id": "CLIENTE_ID", "token": "QzBOdMOhQjFsLUMwcjMtQjRDay1UMGszbgo="}
  headers = ${?HEADERS}

  queryParams = {"ano": 2019, "par1": 1, "par2": 12}
  queryParams = ${?QUERY_PARAMS}

  pathParams: {"cnpj":"23494210000137"}
  pathParams: ${?PATH_PARAMS}

  baseUrl = "https://www.gateway-empresa.com"
  baseUrl = ${?BASE_URL}

  body = ""
  body = ${?FILE_BODY}

  endpoint = "/endpoint/{cnpj}/outro/fim/"
  endpoint = ${?ENDPOINT}

  statusCode = 200
  statusCode = ${?STATUS_CODE}

}

simulation {

  sceneName = "modelo"

  requestName = "stressing events"

  initialUsers = 40
  initialUsers = ${?INITIAL_USERS}

  rampUpRate1 = 40
  rampUpRate1 = ${?RAMP_UP_RATE_1}

  rampUpRate2 = 1000
  rampUpRate2 = ${?RAMP_UP_RATE_2}

  duration = 90 second
  duration = ${?DURATION}

  duration2 = 120 second
  duration2 = ${?DURATION2}

  failedRequestPercent = 10
  failedRequestPercent = ${?FAILED_PERCENT}

}


``` 

**Observe que todos os valores podem ser sobrescritos por argumentos de entrada da _JVM_.**

A classe de simulação ("_simulations.TestConcurrentUsers.scala"_), por sua vez, possui o seguinte formado:

```Scala
class TestConcurrentUsers extends Simulation with SpecTrait {

  lazy val configFile: String = Properties.envOrElse("configFile", "config-file.conf")
  lazy val specs: base.RestSpecs = Specs.restSpecs.apply(ConfigFactory.load(configFile))
  lazy val simulSpecs: base.SimulationSpecs = Specs.simulationsSpecs.apply(ConfigFactory.load(configFile))

  lazy val scene: ScenarioBuilder = scenario(simulSpecs.sceneName)
    .exec(http(simulSpecs.requestName)
      .post(specs.endpoint)
      .headers(specs.headers)
      .queryParamMap(specs.queryParams)
      .body(ElFileBody(specs.fileBody)).asJson
      .check(status.is(200))
    )
    .exec { session2 =>
      println("session:" + session2.toString)
      session2
    }

  setUp(
    scene.inject(
      rampConcurrentUsers(simulSpecs.rampUpRate1)
        .to(simulSpecs.rampUpRate2)
        .during(simulSpecs.duration))
      .protocols(http.baseUrl(specs.baseUrl))
  ).assertions(forAll.failedRequests.percent.lte(simulSpecs.failedPercentRequests))

}
```

### Executar

**Todos os testes:**
```
sbt "gatling:test"
```

**Uma única classe de teste:**
```
sbt "gatling:testOnly simulations.TestConcurrentUsers"
```

**Relatórios:**
```
sbt "gatling:lastReport"
```

**Jar:** 

Compilar o jar:

```
it:assembly
```

Isso gerará um arquivo **_jar_** no diretório especificado em **build.sbt**.

E para executá-lo:

```
java -jar nomeJar.jar -s package.class
```

**Exemplos:**

De forma simplificada:
```
java -jar target/scala-2.12/load-test.jar  -s simulations.TestConcurrentUsers
```
Ou, com parêmetros:
```
java -DconfigFileLogin=exemplo.conf -jar target/scala-2.12/ProjetoModelo-test-1.1.jar -rsf $PWD/src/main/resources/ -s tests.TestConcurrentUsers
```

**Containder Docker**

Build:
```
docker build -t nome_container .
```
Execução:
```
docker run --name gatling_exemplo --rm nome_container -s package.class
```