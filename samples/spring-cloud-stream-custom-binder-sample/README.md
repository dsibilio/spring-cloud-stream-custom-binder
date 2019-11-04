# Spring Cloud Stream Custom Binder Sample

This is a basic sample of usage of a custom Spring Cloud Stream binder.

## Quickstart

1. Install the *it.dsibilio:spring-cloud-stream-custom-binder* artifact in your local repository by moving to the main project folder and running: `mvn clean install`
2. Move back to the `spring-cloud-stream-custom-binder-sample` directory and run the application with: `mvn spring-boot:run`

The application should have created two new files *(output and archive)* in the project folder, demonstrating it consumed messages from the input file, processed them and wrote the result to the output file, eventually storing previously read messages in the *archive.txt* file.

## How does it work

This application relies on a *custom file-based Spring Cloud Stream binder*, that reads the last available message from a file matching the input destination name, and writing the result of the `SpringCloudStreamCustomBinderSampleApplication.handle(String)` method to the file that matches the output destination name.
It also keeps track of all messages that have been previously read by writing them to the archive.txt file in the project folder, and discards identical messages if consequent.

### Reference Documentation
For further reference, please consider the following sources:

* [Spring Cloud Stream Reference Guide](https://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/)

