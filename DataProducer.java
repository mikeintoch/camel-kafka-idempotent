// camel-k: language=java property-file=producer.properties
// camel-k: dependency=camel:http
// camel-k: dependency=camel:gson
// camel-k: dependency=camel:csv
// camel-k: dependency=mvn:javax.servlet:servlet-api:jar:2.5
// camel-k: dependency=mvn:commons-logging:commons-logging:jar:1.2

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;


public class DataProducer extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    // This is the actual route
    from("timer:java?period={{period}}")
        // We start by reading our data.csv file, looping on each row
        .to("{{source.csv}}").unmarshal("customCSV").split(body()).streaming()
        // Marshalling to json    
        .marshal().json(JsonLibrary.Gson)
        // Send message to kafka broker
        .to("kafka:duplicate-topic-data")
        // Write some log to know it finishes properly
        .log("Information was sent");
  }
}
