package br.edu.ifpr.tempconv.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;

import br.edu.ifpr.tempconv.model.Temperature;
import br.edu.ifpr.tempconv.model.types.TemperatureTypes;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TemperatureRepository {

    private final MongoCollection<Document> collection;

    public TemperatureRepository() {
        // TODO: Obter URI do MongoDB de uma forma mais configurável (e.g., MicroProfile Config)
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("tempconvdb");
        this.collection = database.getCollection("temperatures");
    }

    public Temperature save(Temperature temperature) {
        Document doc = toDocument(temperature);
        if (temperature.getId() == null) {
            temperature.setId(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
            doc.put("_id", temperature.getId());
            collection.insertOne(doc);
        } else {
            collection.replaceOne(Filters.eq("_id", temperature.getId()), doc);
        }
        return temperature;
    }

    public Optional<Temperature> findById(Long id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return Optional.ofNullable(fromDocument(doc));
    }

    public List<Temperature> findAll() {
        return collection.find()
                .map(this::fromDocument)
                .into(new ArrayList<>());
    }

    public List<Temperature> findByAttributes(Double tempi, TemperatureTypes typei, Double tempo, TemperatureTypes typeo) {
        List<Bson> filters = new ArrayList<>();
        if (tempi != null) filters.add(Filters.eq("tempi", tempi));
        if (typei != null) filters.add(Filters.eq("typei", typei.name()));
        if (tempo != null) filters.add(Filters.eq("tempo", tempo));
        if (typeo != null) filters.add(Filters.eq("typeo", typeo.name()));

        if (filters.isEmpty()) {
            return findAll();
        }

        return collection.find(Filters.and(filters))
                .map(this::fromDocument)
                .into(new ArrayList<>());
    }

    public boolean update(Temperature temperature) {
        if (temperature.getId() == null) {
            return false;
        }
        Document doc = toDocument(temperature);
        UpdateResult result = collection.replaceOne(Filters.eq("_id", temperature.getId()), doc);
        return result.wasAcknowledged() && result.getModifiedCount() > 0;
    }

    public boolean deleteById(Long id) {
        DeleteResult result = collection.deleteOne(Filters.eq("_id", id));
        return result.wasAcknowledged() && result.getDeletedCount() > 0;
    }

    public boolean deleteByTimestamp(Long timestamp) {
        // Assuming the timestamp is used as the _id, which is a Long.
        DeleteResult result = collection.deleteOne(Filters.eq("_id", timestamp));
        return result.wasAcknowledged() && result.getDeletedCount() > 0;
    }

    public int deleteAll() {
        DeleteResult result = collection.deleteMany(new Document());
        return (int) result.getDeletedCount();
    }

    private Document toDocument(Temperature temperature) {
        Document doc = new Document();
        if (temperature.getId() != null) {
            doc.put("_id", temperature.getId());
        }
        doc.put("timestamp", temperature.getTimestamp().toString()); // Store as String for simplicity
        doc.put("tempi", temperature.getTempi());
        doc.put("typei", temperature.getTypei().name());
        doc.put("tempo", temperature.getTempo());
        doc.put("typeo", temperature.getTypeo().name());
        return doc;
    }

    private Temperature fromDocument(Document doc) {
        if (doc == null) {
            return null;
        }
        Temperature temperature = new Temperature();
        temperature.setId(doc.getLong("_id"));
        temperature.setTimestamp(LocalDateTime.parse(doc.getString("timestamp")));
        temperature.setTempi(doc.getDouble("tempi"));
        temperature.setTypei(TemperatureTypes.valueOf(doc.getString("typei")));
        temperature.setTempo(doc.getDouble("tempo"));
        temperature.setTypeo(TemperatureTypes.valueOf(doc.getString("typeo")));
        return temperature;
    }
}

