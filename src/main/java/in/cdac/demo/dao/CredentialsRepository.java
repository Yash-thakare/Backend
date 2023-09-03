package in.cdac.demo.dao;

import org.springframework.data.repository.CrudRepository;

import in.cdac.demo.entity.Course;
import in.cdac.demo.entity.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, String>{

}
