package com.fairanb.event;

import com.fairanb.utils.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleSourceBean {
	private Source source;

	private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

	@Autowired
	public SimpleSourceBean(Source source) {
		this.source = source;
	}

	public void publishMerchantChange(String action, Long id) {
		logger.debug("Sending Kafka message {} for Merchant Id: {}", action, id);
		MerchantChangeModel change = new MerchantChangeModel(MerchantChangeModel.class.getTypeName(), action, id,
				UserContext.getCorrelationId());

		source.output().send(MessageBuilder.withPayload(change).build());
	}

	public void publishPromoCodeChange(String action, Long id) {
		logger.debug("Sending Kafka message {} for Promo code Id: {}", action, id);
		PromoCodeChangeModel change = new PromoCodeChangeModel(PromoCodeChangeModel.class.getTypeName(), action, id,
				UserContext.getCorrelationId());

		source.output().send(MessageBuilder.withPayload(change).build());
	}

	public void publishDiscountDefinitionChange(String action, Long id) {
		logger.debug("Sending Kafka message {} for Discount definition Id: {}", action, id);
		DiscountDefinitionChangeModel change = new DiscountDefinitionChangeModel(
				DiscountDefinitionChangeModel.class.getTypeName(), action, id, UserContext.getCorrelationId());

		source.output().send(MessageBuilder.withPayload(change).build());
	}

	public void publishDefinitionConditionChange(String action, Long id) {
		logger.debug("Sending Kafka message {} for Discount definition Id: {}", action, id);
		DiscountConditionChangeModel change = new DiscountConditionChangeModel(
				DiscountConditionChangeModel.class.getTypeName(), action, id, UserContext.getCorrelationId());

		source.output().send(MessageBuilder.withPayload(change).build());
	}

	public void publishDefinitionResultChange(String action, Long id) {
		logger.debug("Sending Kafka message {} for Discount definition Id: {}", action, id);
		DiscountResultChangeModel change = new DiscountResultChangeModel(DiscountResultChangeModel.class.getTypeName(),
				action, id, UserContext.getCorrelationId());

		source.output().send(MessageBuilder.withPayload(change).build());
	}
}
