package app.dao

import com.mongodb.BasicDBObject
import com.mongodb.Mongo
import com.mongodb.util.JSON
import groovy.stream.Stream
import org.bson.types.ObjectId
/**
 * User: danielwoods
 * Date: 7/31/13
 */
class PeopleDAO extends AbstractMongoDAO {

    PeopleDAO(Mongo mongo, String database) {
        super(mongo, database)
    }

    List<String> getAll(List<String> fields) {
        BasicDBObject fieldsObj = fields.inject([:]) { obj, String name ->
            if (!obj) obj = new BasicDBObject(name, 1)
            else ((BasicDBObject)obj).append(name, 1)
            obj
        }

        def curs = coll.find(new BasicDBObject(), fieldsObj)
        def s = Stream.from curs map { item -> JSON.serialize(item) }
        s.collect()
    }

    String getPerson(String id) {
        BasicDBObject obj = [_id: new ObjectId(id)] as BasicDBObject
        JSON.serialize(coll.findOne(obj))
    }

    BasicDBObject save(String json) {
        def obj = JSON.parse(json) as BasicDBObject
        coll.insert obj
        obj
    }

    @Override
    String getCollectionName() {
        'people'
    }
}
