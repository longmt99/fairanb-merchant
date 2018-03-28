package com.fairanb.service;

import com.fairanb.model.ReturnPolicy;
import com.fairanb.model.ReturnType;
import com.fairanb.model.request.ReturnPolicyRequest;
import com.fairanb.repository.MerchantRepository;
import com.fairanb.repository.ReturnPolicyRepository;
import com.fairanb.repository.ReturnTypeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ReturnPolicyServiceImpl implements ReturnPolicyService {
	@Autowired
	private ReturnPolicyRepository repository;
	@Autowired
	private ReturnTypeRepository returnTypeRepository;
	@Autowired
	private MerchantRepository merchantRepository;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	@Override
	public ReturnPolicy createReturnPolicy(ReturnPolicyRequest request) throws JsonProcessingException {

		ReturnType returnType = returnTypeRepository.findOne(request.getReturnTypeId());
		Objects.requireNonNull(returnType);

		ReturnPolicy returnPolicy = mapReturnPolicy(request, returnType);

		returnPolicy = repository.save(returnPolicy);

		return returnPolicy;

	}

	@Override
	public List<ReturnPolicy> getMerchantReturnPolicies(Long merchantId) {
		List<ReturnPolicy> policies = repository.findByMerchantId(merchantId);
		if (policies.isEmpty()) {
			Objects.requireNonNull(merchantRepository.findOne(merchantId), "Merchant not found");
		}
		return policies;
	}

	@Override
	public ReturnPolicy getById(Long id) {
		ReturnPolicy policy = repository.findOne(id);
		Objects.requireNonNull(policy, "Return policy not found");
		return policy;
	}

	@Override
	public void delete(Long id) throws NullPointerException {
		ReturnPolicy returnPolicy = repository.findOne(id);
		Objects.requireNonNull(returnPolicy, "Return policy not found");

		returnPolicy.setActive(false);

		repository.save(returnPolicy);
	}

	@Override
	public ReturnPolicy update(Long id, ReturnPolicyRequest request) throws IOException {
		ReturnPolicy returnPolicy = repository.findOne(id);
		Objects.requireNonNull(returnPolicy, "Return policy not found");

		mergeReturnPolicyUpdates(returnPolicy, request);

		return repository.save(returnPolicy);
	}

	private void mergeReturnPolicyUpdates(ReturnPolicy returnPolicy, ReturnPolicyRequest upd) throws IOException {
		if (upd.getContactWithin() != null
				|| upd.getRestockingFee() != null
				|| upd.getReturnShippingType() != null
				|| upd.getReturnTypeId() != null) {
			ReturnPolicy.ValueDto valueDto = OBJECT_MAPPER.readValue(returnPolicy.getValue(), ReturnPolicy.ValueDto.class);

			if (upd.getReturnTypeId() != null) {
				ReturnType returnType = returnTypeRepository.findOne(upd.getReturnTypeId());
				Objects.requireNonNull(returnType, "Invalid return type id");
				valueDto.setReturnType(returnType.getName());
			}

			if (upd.getContactWithin() != null) {
				valueDto.setContactWithin(upd.getContactWithin());
			}

			if (upd.getRestockingFee() != null) {
				valueDto.setRestockingFee(upd.getRestockingFee());
			}

			if (upd.getReturnShippingType() != null) {
				valueDto.setReturnShippingType(upd.getReturnShippingType());
			}

			returnPolicy.setValue(OBJECT_MAPPER.writeValueAsString(valueDto));
		}

		if (upd.getName() != null) {
			returnPolicy.setName(upd.getName());
		}

		if (upd.getDescription() != null) {
			returnPolicy.setDescription(upd.getDescription());
		}

		if (upd.getIsDefault() != null) {
			returnPolicy.setIsDefault(upd.getIsDefault());
		}

		if (upd.getMerchantId() != null) {
			returnPolicy.setMerchantId(upd.getMerchantId());
		}
	}

	private ReturnPolicy mapReturnPolicy(ReturnPolicyRequest request, ReturnType returnType) throws JsonProcessingException {
		ReturnPolicy returnPolicy = new ReturnPolicy();
		returnPolicy.setName(request.getName());
		returnPolicy.setDescription(request.getDescription());
		returnPolicy.setIsDefault(request.getIsDefault());
		returnPolicy.setMerchantId(request.getMerchantId());

		ReturnPolicy.ValueDto valueDto = new ReturnPolicy.ValueDto();
		valueDto.setContactWithin(request.getContactWithin());
		valueDto.setReturnType(returnType.getName());
		valueDto.setReturnShippingType(request.getReturnShippingType());
		valueDto.setRestockingFee(request.getRestockingFee());

		returnPolicy.setValue(OBJECT_MAPPER.writeValueAsString(valueDto));

		return returnPolicy;
	}
}
