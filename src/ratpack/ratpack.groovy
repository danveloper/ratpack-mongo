import app.DAOModule
import app.dao.PeopleDAO

import static com.mongodb.util.JSON.serialize
import static groovy.json.JsonOutput.toJson
import static org.ratpackframework.groovy.RatpackScript.ratpack

ratpack {
    modules {
        register new DAOModule(new File('./config', 'Config.groovy'))
    }

    handlers {
        post("api/person") { PeopleDAO dao ->
            accepts.type("application/json") {
                response.send serialize(dao.save(request.text))
            }.send()
        }

        get("api/person/:id?") { PeopleDAO dao ->
            def id = pathTokens.id

            accepts.type("application/json") {
                if (id) {
                    def pers = dao.getPerson(id)
                    if (!pers) {
                        clientError 404
                        return
                    } else {
                        response.send pers
                    }
                } else {
                    response.send toJson(dao.getAll(['name', 'image']))
                }
            }.send()
        }

    }
}
