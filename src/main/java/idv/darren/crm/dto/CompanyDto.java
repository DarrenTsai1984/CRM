package idv.darren.crm.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDto {

	private Long id;
	private String name;
	private String address;
	private String createdBy;
	private Date createdAt;
	private String updatedBy;
	private Date updatedAt;
}
