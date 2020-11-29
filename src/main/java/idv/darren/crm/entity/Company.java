package idv.darren.crm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Company extends BaseEntity {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String address;

}
