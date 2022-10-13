package org.backend.wrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.backend.wrapper.controller.LegacyServiceWrapperController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	private TestRestTemplate template;

	private RestTemplate remoteTemplate;

	@BeforeEach
	void setUp() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setOutputStreaming(false);

		template.getRestTemplate().setRequestFactory(requestFactory);

		remoteTemplate = new RestTemplate();
		remoteTemplate.setErrorHandler(new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return false;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {

			}
		});
		remoteTemplate.setRequestFactory(requestFactory);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void helloMessages_shouldBeSuccesful() {

		ResponseEntity<String> responseMessage = template.getForEntity("/", String.class);
		Assertions.assertEquals(responseMessage.getStatusCode(), HttpStatus.OK);
		Assertions.assertNotNull(responseMessage.getBody());
		Assertions.assertTrue(responseMessage.getBody().contains("Hello"));
		Assertions.assertTrue(responseMessage.getBody().contains("Backend Service"));

		responseMessage = template.getForEntity("/another", String.class);
		Assertions.assertEquals(responseMessage.getStatusCode(), HttpStatus.OK);
		Assertions.assertNotNull(responseMessage.getBody());
		Assertions.assertTrue(responseMessage.getBody().contains("Hello"));
		Assertions.assertTrue(responseMessage.getBody().contains("Backend Service"));

		responseMessage = template.getForEntity("/another-one", String.class);
		Assertions.assertEquals(responseMessage.getStatusCode(), HttpStatus.OK);
		Assertions.assertNotNull(responseMessage.getBody());
		Assertions.assertTrue(responseMessage.getBody().contains("Hello"));
		Assertions.assertTrue(responseMessage.getBody().contains("Backend Service"));
	}

	@Test
	void wrapperLogin() {

		final String url = "/authenticate/login";
		final ResponseEntity<byte[]> responsePost =
				template.postForEntity(
						url,
						new HttpEntity<>(new HashMap<>()),
						byte[].class);

		ResponseEntity<byte[]> responsePostWrapper =
				remoteTemplate.postForEntity(
						LegacyServiceWrapperController.REDIRECT_URL + url,
						new HttpEntity<>(new HashMap<>()),
						byte[].class);

		Assertions.assertEquals(responsePost.getStatusCode(), responsePostWrapper.getStatusCode());
		Assertions.assertArrayEquals(responsePost.getBody(), responsePostWrapper.getBody());
		Assertions.assertTrue(areEqual(responsePost.getHeaders().toSingleValueMap(),
				responsePostWrapper.getHeaders().toSingleValueMap()));
	}

	@Test
	void wrapperLoginGetWithParameters() {

		final String url = "/authenticate/login?q=0";
		final ResponseEntity<byte[]> responsePost =
				template.getForEntity(
						url,
						byte[].class);

		ResponseEntity<byte[]> responsePostWrapper =
				remoteTemplate.getForEntity(
						LegacyServiceWrapperController.REDIRECT_URL + url,
						byte[].class);

		Assertions.assertEquals(responsePost.getStatusCode(), responsePostWrapper.getStatusCode());
		Assertions.assertArrayEquals(responsePost.getBody(), responsePostWrapper.getBody());
		Assertions.assertTrue(areEqual(responsePost.getHeaders().toSingleValueMap(),
				responsePostWrapper.getHeaders().toSingleValueMap()));
	}

	private static boolean areEqual(Map<String, String> first, Map<String, String> second) {
		if (first.size() != second.size()) {
			return false;
		}

		return first.entrySet().stream()
				.allMatch(e -> e.getValue().replace(" ", "")
						.equals(second.get(e.getKey()).replace(" ", "")));
	}

}
