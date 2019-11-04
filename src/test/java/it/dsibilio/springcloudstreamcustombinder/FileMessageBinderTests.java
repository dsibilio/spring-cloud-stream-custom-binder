package it.dsibilio.springcloudstreamcustombinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import it.dsibilio.springcloudstreamcustombinder.config.FileMessageBinderConfiguration;
import it.dsibilio.springcloudstreamcustombinder.producers.FileMessageProducer;
import it.dsibilio.springcloudstreamcustombinder.provisioners.FileMessageBinderProvisioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.ConsumerProperties;
import org.springframework.cloud.stream.binder.ProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.context.Lifecycle;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.nio.file.StandardOpenOption.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { FileMessageBinderConfiguration.class })
public class FileMessageBinderTests {

    private static final String INPUT_DESTINATION = "input.txt";
    private static final String OUTPUT_DESTINATION = "output.txt";
    @Autowired
    private FileMessageBinderProvisioner fileMessageBinderProvisioner;
    @Autowired
    private FileMessageBinder fileMessageBinder;

    @BeforeEach
    public void setUp() {
        cleanUp(OUTPUT_DESTINATION, INPUT_DESTINATION, FileMessageProducer.ARCHIVE);
    }

    @Test
    public void messageHandlerShouldWriteToFile() throws Exception {
        assertNotNull(fileMessageBinderProvisioner);
        assertNotNull(fileMessageBinder);

        ProducerProperties producerProperties = new ProducerProperties();
        ProducerDestination producerDestination = 
                fileMessageBinderProvisioner.provisionProducerDestination(OUTPUT_DESTINATION, producerProperties);

        final MessageHandler messageHandler =
                fileMessageBinder.createProducerMessageHandler(producerDestination, producerProperties, null);

        final String payload = "Fingers crossed";

        final Message<byte[]> message = MessageBuilder
                .withPayload(payload.getBytes())
                .build();

        messageHandler.handleMessage(message);

        String destinationFileContent = new String(Files.readAllBytes(Paths.get(OUTPUT_DESTINATION)));
        assertThat(destinationFileContent.trim()).isEqualTo(payload);
    }

    @Test
    public void messageProducerShouldArchiveReadMessages() throws Exception {
        assertNotNull(fileMessageBinderProvisioner);
        assertNotNull(fileMessageBinder);

        String payload = "Fingers crossed";
        Files.write(Paths.get(INPUT_DESTINATION), payload.getBytes(), CREATE);

        ConsumerProperties consumerProperties = new ConsumerProperties();
        ConsumerDestination consumerDestination = 
                fileMessageBinderProvisioner.provisionConsumerDestination(INPUT_DESTINATION, null, consumerProperties);

        MessageProducer messageProducer = 
                fileMessageBinder.createConsumerEndpoint(consumerDestination, null, consumerProperties);
        ((Lifecycle)messageProducer).start();

        Thread.sleep(50);

        String archivedMessage = new String(Files.readAllBytes(Paths.get(FileMessageProducer.ARCHIVE)));
        assertThat(archivedMessage.trim()).isEqualTo(payload);
    }

    private void cleanUp(String... destinations) {
        for(String destination : destinations) {
            try {
                Files.deleteIfExists(Paths.get(destination));
            } catch (IOException e) {
                // Life goes on...
            }
        }
    }

}
