# Spring Cloud Stream Custom Binder

This is a practical example that shows how to build a **custom Spring Cloud Stream binder** which reads from input files that match the destination name, writes to output files that match the destination name and keeps track of previously read messages in an *archive.txt* file.

## Quickstart

1. Install the artifact in your local repository by running: `mvn clean install`
2. Import the `it.dsibilio:spring-cloud-stream-custom-binder:0.0.1-SNAPSHOT` dependency in a *client* project that relies on Spring Cloud Stream for communication
3. Add a file to the *client* project classpath with its name matching the one of your destination name *(eg. spring.cloud.stream.bindings.***my-input*** requires a ***my-input*** file in the classpath)*
4. Write something to this file and verify that your application consumes the event!

### Reference Documentation
For further reference, please consider the following sources:

* [Spring Cloud Stream - Binders SPI](https://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/#spring-cloud-stream-overview-binder-api)
