package es.project.Pandemic.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.project.Pandemic.EntidadesYClasesSecundarias.Persona;

@Repository
public interface RepositorioPersonas extends JpaRepository<Persona, Long>{
	
	@Query(value = "SELECT CONCAT(apellidos, ', ', nombre) FROM persona WHERE documentoidentidad = :documentoIdentidad ", nativeQuery = true)
	String getNombreUsuario(@Param("documentoIdentidad")String documentoIdentidad);
	
	@Query(value = "SELECT * FROM persona WHERE documentoidentidad = :documentoIdentidad ", nativeQuery = true)
	Persona getPersonaPorDocumentoIdentidad(@Param("documentoIdentidad")String documentoIdentidad);
	
	@Query(value = "SELECT * FROM persona WHERE numeroseguridadsocial = :numeroSeguridadSocial ", nativeQuery = true)
	Persona getPersonaPorNumeroSeguridadSocial(@Param("numeroSeguridadSocial")String numeroSeguridadSocial);
	
	@Query(value = "SELECT * FROM persona WHERE id = :idPersona ", nativeQuery = true)
	Persona getPersona(@Param("idPersona")long idPersona);
	
	@Query(value = "SELECT * FROM persona WHERE usuario = :idUsuario ", nativeQuery = true)
	Persona getPersonaConUsuario(@Param("idUsuario")long idUsuario);
	
	@Query(value = "SELECT COUNT(1) FROM persona WHERE numeroseguridadsocial = :numeroSeguridadSocial ", nativeQuery = true)
	Integer existePersona(@Param("numeroSeguridadSocial")String numeroSeguridadSocial);
	
}