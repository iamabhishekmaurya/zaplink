package io.zaplink.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest @Import(TestConfig.class)
class ZaplinkAuthServiceApplicationTests
{
	@MockitoBean
	private KafkaTemplate<String, Object> kafkaTemplate;
	@Test
	void contextLoads()
	{
	}
}
