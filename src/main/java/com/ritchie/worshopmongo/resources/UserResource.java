package com.ritchie.worshopmongo.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ritchie.worshopmongo.domain.Post;
import com.ritchie.worshopmongo.domain.User;
import com.ritchie.worshopmongo.dto.UserDTO;
import com.ritchie.worshopmongo.services.UserService;

@RestController
@RequestMapping(value="/users")
public class UserResource {
	
	@Autowired
	private UserService service;

	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<UserDTO>> findAll(){
		List<User> list = service.findAll();
		List<UserDTO> listDto = list.stream().map(x -> new UserDTO(x)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
	@RequestMapping( value="/{id}",  method=RequestMethod.GET)
	public ResponseEntity<UserDTO> findById(@PathVariable String id){
		User user = service.findById(id);
		return ResponseEntity.ok().body(new UserDTO(user));
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody UserDTO objDto){
		User user = service.fromDTO(objDto);
		user = service.insert(user);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping( value="/{id}",  method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable String id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping( value="/{id}")
	public ResponseEntity<Void> update(@RequestBody UserDTO objDto, @PathVariable String id){
		User user = service.fromDTO(objDto);
		user.setId(id);
		user = service.update(user);
		
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping( value="/{id}/posts",  method=RequestMethod.GET)
	public ResponseEntity<List<Post>> findPosts(@PathVariable String id){
		User user = service.findById(id);
		return ResponseEntity.ok().body(user.getPosts());
	}
	
}
