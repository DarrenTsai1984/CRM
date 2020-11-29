package idv.darren.crm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import idv.darren.crm.dao.ClientRepository;
import idv.darren.crm.dto.ClientDto;
import idv.darren.crm.entity.Client;
import idv.darren.crm.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	public Client save(ClientDto clientDto) {
		Client client = new Client();
		BeanUtils.copyProperties(clientDto, client);
		return clientRepository.save(client);
	}

	@Override
	public List<Client> saveClients(List<ClientDto> clientDtoList) {
		List<Client> clientList = new ArrayList<>();
	  for (ClientDto clientDto : clientDtoList) {
			Client client = new Client();
			BeanUtils.copyProperties(clientDto, client);
			clientList.add(client);
		}
		return clientRepository.saveAll(clientList);
	}
	
	@Override
	public Client update(Long id, ClientDto clientDto) {
		Client client = clientRepository.findById(id).orElse(null);
		BeanUtils.copyProperties(clientDto, client, "id");
		return clientRepository.save(client);
	}

	@Override
	public Optional<Client> getClientById(Long id) {
		return clientRepository.findById(id);
	}

	@Override
	public void delete(Long id) {
		clientRepository.deleteById(id);
	}

}
