package idv.darren.crm.client;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import idv.darren.crm.CrmApplication;
import idv.darren.crm.dao.ClientRepository;
import idv.darren.crm.dto.ClientDto;
import idv.darren.crm.entity.Client;

@SpringBootTest(classes = CrmApplication.class)
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class ClientControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ClientRepository clientRepository;

	@Test
	@WithMockUser(username = "darren", password = "123", roles = { "OPERATOR" })
	public void operatorTest() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		ClientDto clientDto = new ClientDto();
		clientDto.setCompanyId(Long.valueOf("1"));
		clientDto.setName("Google");
		clientDto.setEmail("google@gmail.com");
		clientDto.setPhone("123123");

		String jsonString = mockMvc
				.perform(post("/client/add").with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(clientDto)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").value(clientDto.getName())).andReturn()
				.getResponse().getContentAsString();

		Client client = mapper.readValue(jsonString, Client.class);

		mockMvc.perform(get("/client/get/" + client.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(client.getName()));

		clientDto.setName("Apple");

		mockMvc.perform(put("/client/update").with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(clientDto))).andDo(print()).andExpect(status().isForbidden());

		mockMvc.perform(delete("/client/delete/" + client.getId())).andDo(print()).andExpect(status().isForbidden());

		addMultiClients();

		clientRepository.delete(client);
	}

	private void addMultiClients() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		List<String> nameList = Arrays.asList("John", "Mary", "Andy");

		List<ClientDto> clientDtoList = new ArrayList<>();
		for (int i = 1; i <= nameList.size(); i++) {
			ClientDto clientDto = new ClientDto();
			clientDto.setCompanyId(new Long(i));
			clientDto.setName(nameList.get(i - 1));
			clientDto.setEmail(new StringBuilder(nameList.get(i - 1)).append("@gmail.com").toString());
			clientDto.setPhone("123123123");
			clientDtoList.add(clientDto);
		}

		String content = mockMvc
				.perform(post("/client/add/multiple").with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(clientDtoList)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andReturn().getResponse()
				.getContentAsString();

		List<Client> clientList = mapper.readValue(content, new TypeReference<List<Client>>() {
		});

		clientRepository.deleteAll(clientList);
	}

	@Test
	@WithMockUser(username = "ted", password = "123", roles = { "MANAGER" })
	public void managerTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		mockMvc.perform(post("/client/add").with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new ClientDto()))).andDo(print()).andExpect(status().isForbidden());

		Client client = new Client();
		client.setCompanyId(Long.valueOf("1"));
		client.setName("Apple");
		client.setEmail("apple@gmail.com");
		client.setPhone("456456456");
		client = clientRepository.save(client);

		ClientDto clientDto = new ClientDto();
		BeanUtils.copyProperties(client, clientDto);
		clientDto.setName("Google");

		mockMvc
				.perform(put("/client/update/" + client.getId()).with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(clientDto)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Google"));

		mockMvc.perform(get("/client/get/" + client.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Google"));

		mockMvc.perform(delete("/client/delete/" + client.getId())).andDo(print()).andExpect(status().isOk());
		assertNull(clientRepository.findById(client.getId()).orElse(null));
	}

	@Test
	@WithMockUser(username = "root", password = "root", roles = { "SUPER_USER" })
	public void superUserTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		ClientDto clientDto = new ClientDto();
		clientDto.setCompanyId(Long.valueOf("2"));
		clientDto.setName("Amazon");
		clientDto.setEmail("amazon@gmail.com");
		clientDto.setPhone("789789789");

		String jsonString = mockMvc
				.perform(post("/client/add").with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(clientDto)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").value(clientDto.getName())).andReturn()
				.getResponse().getContentAsString();

		Client client = mapper.readValue(jsonString, Client.class);

		clientDto.setName("Microsoft");
		mockMvc
				.perform(put("/client/update/" + client.getId()).with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(clientDto)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Microsoft"));

		mockMvc.perform(get("/client/get/" + client.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Microsoft"));

		mockMvc.perform(delete("/client/delete/" + client.getId())).andDo(print()).andExpect(status().isOk());
		assertNull(clientRepository.findById(client.getId()).orElse(null));
	}
}
