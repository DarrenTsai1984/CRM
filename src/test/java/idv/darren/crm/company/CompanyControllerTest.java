package idv.darren.crm.company;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import idv.darren.crm.CrmApplication;
import idv.darren.crm.dao.CompanyRepository;
import idv.darren.crm.dto.CompanyDto;
import idv.darren.crm.entity.Company;
import idv.darren.crm.service.CompanyService;

@SpringBootTest(classes = CrmApplication.class)
@EnableAutoConfiguration
@AutoConfigureMockMvc
public class CompanyControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private CompanyService companyService;

	@Test
	@WithMockUser(username = "darren", password = "123", roles = { "OPERATOR" })
	public void operatorTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		CompanyDto companyDto = new CompanyDto();
		companyDto.setName("Google");
		companyDto.setAddress("Google");

		String jsonString = mockMvc
				.perform(post("/company/add").with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(companyDto)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").value(companyDto.getName())).andReturn()
				.getResponse().getContentAsString();

		Company company = mapper.readValue(jsonString, Company.class);

		mockMvc.perform(get("/company/get/" + company.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(company.getName()));

		companyDto.setAddress("apple");

		mockMvc.perform(put("/company/update").with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(companyDto))).andDo(print()).andExpect(status().isForbidden());

		mockMvc.perform(delete("/company/delete/" + company.getId())).andDo(print()).andExpect(status().isForbidden());

		companyRepository.delete(company);
	}

	@Test
	@WithMockUser(username = "ted", password = "123", roles = { "MANAGER" })
	public void managerTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		mockMvc.perform(post("/company/add").with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(new CompanyDto()))).andDo(print()).andExpect(status().isForbidden());

		CompanyDto companyDto = new CompanyDto();
		companyDto.setName("Apple");
		companyDto.setAddress("Apple");
		Company company = companyService.save(companyDto);

		companyDto.setAddress("Google");

		mockMvc
				.perform(put("/company/update/" + company.getId()).with(csrf().asHeader())
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(companyDto)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.address").value("Google"));

		mockMvc.perform(get("/company/get/" + company.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.address").value("Google"));

		mockMvc.perform(delete("/company/delete/" + company.getId())).andDo(print()).andExpect(status().isOk());
		assertNull(companyRepository.findById(company.getId()).orElse(null));
	}

	@Test
	@WithMockUser(username = "root", password = "root", roles = { "SUPER_USER" })
	public void superUserTest() throws Exception {
		ObjectMapper mapper = new ObjectMapper();

		CompanyDto companyDto = new CompanyDto();
		companyDto.setName("Amazon");
		companyDto.setAddress("Amazon");

		String jsonString = mockMvc
				.perform(post("/company/add").with(csrf().asHeader()).contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(companyDto)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").value(companyDto.getName())).andReturn()
				.getResponse().getContentAsString();

		Company company = mapper.readValue(jsonString, Company.class);

		companyDto.setAddress("Microsoft");
		mockMvc
				.perform(put("/company/update/" + company.getId()).with(csrf().asHeader())
						.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(companyDto)))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.address").value("Microsoft"));

		mockMvc.perform(get("/company/get/" + company.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.address").value("Microsoft"));

		mockMvc.perform(delete("/company/delete/" + company.getId())).andDo(print()).andExpect(status().isOk());
		assertNull(companyRepository.findById(company.getId()).orElse(null));
	}
}
