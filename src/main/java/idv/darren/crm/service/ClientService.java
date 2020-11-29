package idv.darren.crm.service;

import java.util.List;
import java.util.Optional;

import idv.darren.crm.dto.ClientDto;
import idv.darren.crm.entity.Client;

public interface ClientService {

	Client save(ClientDto clientDto);
	
	List<Client> saveClients(List<ClientDto> clientDtoList);
	
	Client update(Long id, ClientDto clientRepository);
	
	Optional<Client> getClientById(Long id);
	
	void delete(Long id);
}
