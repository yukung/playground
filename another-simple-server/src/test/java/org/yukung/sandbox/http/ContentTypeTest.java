package org.yukung.sandbox.http;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author Yusuke Ikeda
 */
public class ContentTypeTest {

    @Test
    public void html() {
        ContentType contentType = ContentType.toContentType("html");

        assertThat(contentType, is(ContentType.TEXT_HTML));
    }

    @Test
    public void htm() {
        ContentType contentType = ContentType.toContentType("htm");

        assertThat(contentType, is(ContentType.TEXT_HTML));
    }

    @Test
    public void css() {
        ContentType contentType = ContentType.toContentType("css");

        assertThat(contentType, is(ContentType.TEXT_CSS));
    }

    @Test
    public void jpeg() {
        ContentType contentType = ContentType.toContentType("jpeg");

        assertThat(contentType, is(ContentType.IMAGE_JPG));
    }

    @Test
    public void unknown() {
        // exercise
        ContentType contentType = ContentType.toContentType("xxxx");

        // verify
        assertThat(contentType, is(ContentType.TEXT_PLAIN));
    }

}