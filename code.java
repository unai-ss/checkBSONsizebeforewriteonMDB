import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Filters;
import java.nio.charset.StandardCharsets;

public class UpdateDocumentExample {
    public static void main(String[] args) {
        // Connection string to your MongoDB instance
        String connectionString = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            MongoDatabase database = mongoClient.getDatabase("yourDatabaseName");
            MongoCollection<Document> collection = database.getCollection("yourCollectionName");

            // Step 1: Retrieve the document to be updated
            Document originalDocument = collection.find(Filters.eq("_id", "yourDocumentId")).first();
            if (originalDocument == null) {
                System.out.println("Document not found.");
                return;
            }

            // Step 2: Modify the document in the application
            // Example modification: adding a new field
            originalDocument.append("newField", "newValue");

            // Step 3: Check the size of the modified document
            long sizeInBytes = originalDocument.toJson().getBytes(StandardCharsets.UTF_8).length;
            if (sizeInBytes < 16777216) { // 16MB limit
                // Step 4: If the size is below 16MB, proceed with the update
                Bson filter = Filters.eq("_id", "yourDocumentId");
                collection.replaceOne(filter, originalDocument);
                System.out.println("Document updated successfully.");
            } else {
                // Handle the error accordingly
                System.out.println("Document exceeds the maximum size limit of 16MB.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
