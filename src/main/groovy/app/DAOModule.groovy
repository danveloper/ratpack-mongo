package app

import app.dao.PeopleDAO
import com.google.inject.AbstractModule
import com.mongodb.Mongo

/**
 * User: danielwoods
 * Date: 7/31/13
 */
class DAOModule extends AbstractModule {

    private static ConfigObject config
    private static Mongo _mongo

    public DAOModule(File cfg) {
        config = new ConfigSlurper().parse(cfg.text).app
    }

    @Override
    protected void configure() {
        bind(PeopleDAO).toInstance(new PeopleDAO(mongo, config.mongo.db as String))
    }

    private static Mongo getMongo() {
        if (_mongo == null) {
            _mongo = new Mongo(config.mongo.host as String, config.mongo.port ?: 27017)
            if (!_mongo.getDB(config.mongo.db).authenticate(config.mongo.user, config.mongo.pass as char[])) {
                throw new RuntimeException("Could not authenticate to Mongo with provided credentials")
            }
        }
        _mongo
    }
}
