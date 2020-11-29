package idv.darren.crm.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import idv.darren.crm.dto.ClientDto;
import idv.darren.crm.entity.Client;
import idv.darren.crm.service.ClientService;

@Controller
@RequestMapping(value = "/client", produces = "application/json; charset=utf-8")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getClient(@PathVariable Long id) {
		Optional<Client> client = clientService.getClientById(id);
		return client.map(response -> ResponseEntity.ok().body(client)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping("/add")
	public ResponseEntity<Client> addClient(@RequestBody ClientDto clientDto) {
		Client client = clientService.save(clientDto);
		return ResponseEntity.ok().body(client);
	}
	
	@PostMapping("/add/multiple")
	public ResponseEntity<List<Client>> addMultiClients(@RequestBody List<ClientDto> clientDto) {
		List<Client> clientList = clientService.saveClients(clientDto);
		return ResponseEntity.ok().body(clientList);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody ClientDto clientDto) {
		Client client = clientService.update(id, clientDto);
		return ResponseEntity.ok().body(client);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteClient(@PathVariable Long id) {
		clientService.delete(id);
		return ResponseEntity.ok().build();
	}
}
