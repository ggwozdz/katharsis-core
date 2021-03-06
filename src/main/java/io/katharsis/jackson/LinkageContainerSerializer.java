package io.katharsis.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.katharsis.resource.registry.ResourceRegistry;
import io.katharsis.response.LinkageContainer;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class LinkageContainerSerializer extends JsonSerializer<LinkageContainer> {

    private ResourceRegistry resourceRegistry;

    public LinkageContainerSerializer(ResourceRegistry resourceRegistry) {
        this.resourceRegistry = resourceRegistry;
    }

    @Override
    public void serialize(LinkageContainer linkageContainer, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        writeType(gen, linkageContainer.getRelationshipClass());
        try {
            writeId(gen, linkageContainer);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        gen.writeEndObject();
    }

    private void writeType(JsonGenerator gen, Class<?> relationshipClass) throws IOException {
        String resourceType = resourceRegistry.getResourceType(relationshipClass);
        gen.writeObjectField("type", resourceType);
    }

    private void writeId(JsonGenerator gen, LinkageContainer linkageContainer)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException {
        Field idField = linkageContainer.getRelationshipEntry().getResourceInformation().getIdField();
        String sourceId = BeanUtils.getProperty(linkageContainer.getObjectItem(), idField.getName());
        gen.writeObjectField("id", sourceId);
    }

    public Class<LinkageContainer> handledType() {
        return LinkageContainer.class;
    }
}
