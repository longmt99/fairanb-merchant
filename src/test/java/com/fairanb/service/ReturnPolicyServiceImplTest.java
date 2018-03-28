package com.fairanb.service;

import com.fairanb.MerchantRestAPI;
import com.fairanb.model.ReturnPolicy;
import com.fairanb.model.ReturnType;
import com.fairanb.model.request.ReturnPolicyRequest;
import com.fairanb.repository.ReturnPolicyRepository;
import com.fairanb.repository.ReturnTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MerchantRestAPI.class)
public class ReturnPolicyServiceImplTest {
	private static final long RETURN_TYPE_ID     = 1L;
	private static final long NEW_RETURN_TYPE_ID = 2L;
	private static final long RETURN_POLICY_ID   = 1;
	private static final long MERCHANT_ID        = 1;

	@Autowired
	ReturnPolicyServiceImpl service;

	@MockBean
	ReturnPolicyRepository returnPolicyRepositoryMock;
	@MockBean
	ReturnTypeRepository   returnTypeRepositoryMock;

	private ReturnType   returnType;
	private ReturnType   newReturnType;
	private ReturnPolicy returnPolicy;

	@Before
	public void setUp() throws Exception {
		returnType = new ReturnType();
		returnType.setMerchantId(MERCHANT_ID);
		returnType.setName("Cancel");
		returnType.setId(RETURN_TYPE_ID);

		newReturnType = new ReturnType();
		newReturnType.setMerchantId(MERCHANT_ID);
		newReturnType.setName("Cancel");
		newReturnType.setId(NEW_RETURN_TYPE_ID);

		this.returnPolicy = new ReturnPolicy();
		returnPolicy.setId(RETURN_POLICY_ID);
		returnPolicy.setValue("{" +
				"\"contactWithin\":5," +
				"\"returnType\":\"Cancel\"," +
				"\"returnShippingType\":\"BUYER_PAYS\"," +
				"\"restockingFee\":10}");

		when(returnTypeRepositoryMock.findOne(RETURN_TYPE_ID)).thenReturn(returnType);
		when(returnTypeRepositoryMock.findOne(NEW_RETURN_TYPE_ID)).thenReturn(newReturnType);

		when(returnPolicyRepositoryMock.save(any(ReturnPolicy.class))).then(invocation -> {
			ReturnPolicy argument = invocation.getArgumentAt(0, ReturnPolicy.class);
			argument.setId(RETURN_POLICY_ID);
			return argument;
		});

		when(returnPolicyRepositoryMock.findOne(RETURN_POLICY_ID)).thenReturn(this.returnPolicy);
		when(returnPolicyRepositoryMock.findByMerchantId(MERCHANT_ID)).thenReturn(Arrays.asList(this.returnPolicy));
	}

	@Test
	public void createReturnPolicy() throws Exception {
		String name               = "test_name";
		String description        = "test description";
		String returnShippingType = "BUYER_PAYS";
		int    restockingFee      = 10;
		long   returnTypeId       = 1L;
		int    contactWithin      = 45;

		ReturnPolicyRequest request = new ReturnPolicyRequest();
		request.setName(name);
		request.setDescription(description);
		request.setIsDefault(false);
		request.setMerchantId(MERCHANT_ID);
		request.setRestockingFee(restockingFee);
		request.setReturnTypeId(returnTypeId);
		request.setContactWithin(contactWithin);
		request.setReturnShippingType(returnShippingType);

		ReturnPolicy actual = service.createReturnPolicy(request);

		Assert.assertEquals(actual.getName(), name);
		Assert.assertEquals(actual.getDescription(), description);
		Assert.assertEquals(actual.getIsDefault(), false);
		Assert.assertThat(actual.getMerchantId(), CoreMatchers.equalTo(MERCHANT_ID));
		Assert.assertThat(actual.getId(), CoreMatchers.equalTo(RETURN_POLICY_ID));

		ObjectMapper objectMapper = new ObjectMapper();

		ReturnPolicy.ValueDto value = objectMapper.readValue(actual.getValue(), ReturnPolicy.ValueDto.class);

		Assert.assertThat(value.getContactWithin(), CoreMatchers.equalTo(contactWithin));
		Assert.assertThat(value.getRestockingFee(), CoreMatchers.equalTo(restockingFee));
		Assert.assertThat(value.getReturnShippingType(), CoreMatchers.equalTo(returnShippingType));
		Assert.assertThat(value.getReturnType(), CoreMatchers.equalTo(this.returnType.getName()));
	}

	@Test
	public void getById() throws Exception {
		ReturnPolicy returnPolicy = service.getById(RETURN_POLICY_ID);

		Assert.assertSame(returnPolicy, this.returnPolicy);
	}

	@Test
	public void getByMerchantId() throws Exception {
		List<ReturnPolicy> returnPolicies = service.getMerchantReturnPolicies(MERCHANT_ID);

		Assert.assertEquals(returnPolicies.size(), 1);
		Assert.assertSame(returnPolicies.get(0), this.returnPolicy);
	}

	@Test
	public void delete() throws Exception {
		ArgumentCaptor<ReturnPolicy> captor = ArgumentCaptor.forClass(ReturnPolicy.class);

		service.delete(RETURN_POLICY_ID);

		Mockito.verify(returnPolicyRepositoryMock).findOne(RETURN_POLICY_ID);
		Mockito.verify(returnPolicyRepositoryMock).save(captor.capture());

		ReturnPolicy argument = captor.getValue();
		Assert.assertSame(argument, returnPolicy);
		Assert.assertEquals(argument.getActive(), false);
	}

	@Test
	public void update() throws Exception {
		ArgumentCaptor<ReturnPolicy> captor = ArgumentCaptor.forClass(ReturnPolicy.class);

		String  newName               = "new_name";
		String  newDescription        = "description";
		boolean newIsDefault          = true;
		long    newMerchantId         = 2L;
		int     newContactWithin      = 2;
		String  newReturnShippingType = "SELLER_PAYS";
		int     newRestockingFee      = 1;

		ReturnPolicyRequest upd = new ReturnPolicyRequest(newName, newDescription, newIsDefault, newMerchantId, newContactWithin, NEW_RETURN_TYPE_ID, newReturnShippingType, newRestockingFee);

		service.update(RETURN_POLICY_ID, upd);

		Mockito.verify(returnPolicyRepositoryMock).findOne(RETURN_POLICY_ID);
		Mockito.verify(returnPolicyRepositoryMock).save(captor.capture());

		ReturnPolicy argument = captor.getValue();
		Assert.assertEquals(argument.getName(), newName);
		Assert.assertEquals(argument.getDescription(), newDescription);
		Assert.assertEquals(argument.getIsDefault(), newIsDefault);
		Assert.assertThat(argument.getMerchantId(), CoreMatchers.equalTo(newMerchantId));

		ObjectMapper objectMapper = new ObjectMapper();

		ReturnPolicy.ValueDto value = objectMapper.readValue(argument.getValue(), ReturnPolicy.ValueDto.class);

		Assert.assertThat(value.getContactWithin(), CoreMatchers.equalTo(newContactWithin));
		Assert.assertThat(value.getRestockingFee(), CoreMatchers.equalTo(newRestockingFee));
		Assert.assertThat(value.getReturnShippingType(), CoreMatchers.equalTo(newReturnShippingType));
		Assert.assertThat(value.getReturnType(), CoreMatchers.equalTo(this.newReturnType.getName()));

	}
}