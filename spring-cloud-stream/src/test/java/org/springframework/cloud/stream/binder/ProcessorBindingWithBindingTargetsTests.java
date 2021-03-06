/*
 * Copyright 2015-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.binder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.utils.MockBinderRegistryConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

/**
 * @author Marius Bogoevici
 * @author Janne Valkealahti
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProcessorBindingWithBindingTargetsTests.TestProcessor.class)
public class ProcessorBindingWithBindingTargetsTests {

	@Autowired
	private BinderFactory binderFactory;

	@Autowired
	private Processor testProcessor;

	@SuppressWarnings("rawtypes")
	@Test
	public void testSourceOutputChannelBound() {
		final Binder binder = binderFactory.getBinder(null, MessageChannel.class);
		verify(binder).bindConsumer(eq("testtock.0"), isNull(),
				eq(this.testProcessor.input()), Mockito.<ConsumerProperties>any());
		verify(binder).bindProducer(eq("testtock.1"), eq(this.testProcessor.output()),
				Mockito.<ProducerProperties>any());
	}

	@EnableBinding(Processor.class)
	@EnableAutoConfiguration
	@Import(MockBinderRegistryConfiguration.class)
	@PropertySource("classpath:/org/springframework/cloud/stream/binder/processor-binding-test.properties")
	public static class TestProcessor {

	}
}
