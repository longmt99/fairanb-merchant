package com.fairanb.service;

import com.fairanb.model.ReturnPolicy;
import com.fairanb.model.request.ReturnPolicyRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface ReturnPolicyService {
	ReturnPolicy createReturnPolicy(ReturnPolicyRequest request) throws JsonProcessingException;

	List<ReturnPolicy> getMerchantReturnPolicies(Long merchantId);

	ReturnPolicy getById(Long id);

	void delete(Long id) throws NullPointerException;

	ReturnPolicy update(Long id, ReturnPolicyRequest request) throws IOException;
}
