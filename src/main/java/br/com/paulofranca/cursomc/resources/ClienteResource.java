package br.com.paulofranca.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.paulofranca.cursomc.dto.ClienteDTO;
import br.com.paulofranca.cursomc.dto.ClienteNewDTO;
import br.com.paulofranca.cursomc.model.Cliente;
import br.com.paulofranca.cursomc.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		Cliente obj = this.service.find(id);

		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO clienteNewDto) {
		Cliente cliente = this.service.fromDTO(clienteNewDto);

		cliente = this.service.insert(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();

		return ResponseEntity.created(uri).build();
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id) {
		Cliente cliente = this.service.fromDTO(objDto);

		cliente.setId(id);
		cliente = this.service.update(cliente);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		this.service.delete(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping()
	public ResponseEntity<List<ClienteDTO>> findAll() {
		List<Cliente> lista = this.service.findAll();

		List<ClienteDTO> listaDto = lista.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());

		return ResponseEntity.ok().body(listaDto);
	}

	@GetMapping(value = "/page")
	public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Cliente> lista = this.service.findPage(page, linesPerPage, orderBy, direction);

		Page<ClienteDTO> listaDto = lista.map(obj -> new ClienteDTO(obj));

		return ResponseEntity.ok().body(listaDto);
	}
}
