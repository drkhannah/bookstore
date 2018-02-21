package com.drkhannah.bookstore.rest;

import com.drkhannah.bookstore.model.Book;
import com.drkhannah.bookstore.model.Language;
import com.drkhannah.bookstore.repository.BookRepository;
import com.drkhannah.bookstore.rest.BookEndpoint;
import com.drkhannah.bookstore.rest.JAXRSConfiguration;
import com.drkhannah.bookstore.util.IsbnGenerator;
import com.drkhannah.bookstore.util.NumberGenerator;
import com.drkhannah.bookstore.util.TextUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Date;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@RunAsClient
public class BookEndpointTest {

    @Deployment(testable = false)
    public static Archive<?> createDeploymentPackage() {

        return ShrinkWrap.create(WebArchive.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addClass(BookRepository.class)
                .addClass(NumberGenerator.class)
                .addClass(IsbnGenerator.class)
                .addClass(TextUtil.class)
                .addClass(BookEndpoint.class)
                .addClass(JAXRSConfiguration.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml");
    }

    @Test
    public void createBook(@ArquillianResteasyResource("api/books") WebTarget webTarget) throws Exception {

        // Test counting books
        Response response = webTarget.path("count").request().get();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());

        // test find all
        response = webTarget.request(APPLICATION_JSON).get();
        assertEquals(NO_CONTENT.getStatusCode(), response.getStatus());

        // create a book
        Book book = new Book("isbn", "a  title", 12F, 123, Language.ENGLISH, new Date(), "imageURL", "description");
        response = webTarget.request(APPLICATION_JSON).post(Entity.entity(book, APPLICATION_JSON));
        assertEquals(CREATED.getStatusCode(), response.getStatus());

    }

}
