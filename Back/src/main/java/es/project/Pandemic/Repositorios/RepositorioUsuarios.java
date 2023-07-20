package es.project.Pandemic.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.project.Pandemic.EntidadesYClasesSecundarias.Rol;
import es.project.Pandemic.EntidadesYClasesSecundarias.Usuario;

@Repository
public interface RepositorioUsuarios extends JpaRepository<Usuario, Long>{
	@Query(value = "SELECT * FROM usuario u WHERE u.documento_identidad = :documentoIdentidad ", nativeQuery = true)
	Usuario findByDocumentoIdentidad(@Param("documentoIdentidad")String documentoIdentidad);
}
