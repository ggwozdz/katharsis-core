package io.katharsis.jackson;

import io.katharsis.resource.mock.models.Project;
import io.katharsis.response.Container;
import org.junit.Test;

import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

public class DataLinksContainerSerializerTest extends BaseSerializerTest {

    @Test
    public void onSimpleObjectShouldIncludeLinksField() throws Exception {
        // GIVEN
        Project project = new Project();

        // WHEN
        String result = sut.writeValueAsString(new Container<>(project));

        // THEN
        assertThatJson(result).node("links").isPresent();
    }

    @Test
    public void onSimpleResourceLinksToDataShouldHaveSelfLink() throws Exception {
        // GIVEN
        Project project = new Project();
        project.setId(1L);

        // WHEN
        String result = sut.writeValueAsString(new Container<>(project));

        // THEN
        assertThatJson(result).node("links.self").isEqualTo("https://service.local/projects/1");
    }
}
