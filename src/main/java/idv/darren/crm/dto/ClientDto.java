package idv.darren.crm.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto {

	private Long id;
	private Long companyId;
	private String name;
	private String email;
	private String phone;
	private String createdBy;
	private Date createdAt;
	private String updatedBy;
	private Date updatedAt;
}
