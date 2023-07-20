package es.project.Pandemic.Repositorios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.project.Pandemic.EntidadesYClasesSecundarias.Rol;


@Repository
public interface RepositorioRoles extends JpaRepository<Rol, Long>{
	
	
	@Query(value = "SELECT DISTINCT rol.id, rol.nombre FROM rolesusuariocentro "
			+ "INNER JOIN rol ON rolesusuariocentro.rol = rol.id "
			+ "WHERE usuario = :idUsuario ", nativeQuery = true)
	List<Rol> getRolesUsuarioSinCentro(@Param("idUsuario")long idUsuario);
	
	@Query(value = "SELECT DISTINCT rol.id, rol.nombre FROM rolesusuariocentro "
			+ "INNER JOIN rol ON rolesusuariocentro.rol = rol.id "
			+ "INNER JOIN usuario AS usuario ON rolesusuariocentro.usuario = usuario.id "
			+ "WHERE usuario.documento_identidad = :documentoIdentidadUsuario ", nativeQuery = true)
	List<Rol> getRolesUsuarioSinCentro(@Param("documentoIdentidadUsuario")String documentoIdentidad);
	
	@Query(value = "SELECT centro FROM rolesusuariocentro WHERE usuario = :idUsuario AND rol = :idRol ", nativeQuery = true)
	List<Long> getCentrosParaRolUsuario(@Param("idUsuario")long idUsuario,@Param("idRol")long idRol);
	
	@Query(value = "SELECT usuario FROM rolesusuariocentro WHERE centro = :idCentro AND rol = 1 ", nativeQuery = true)
	List<Long> getPlantillaDeCentro(@Param("idCentro")Long idCentro);
	
	@Query(value = "SELECT usuario FROM rolesusuariocentro WHERE centro = :idCentro AND rol = 2 ", nativeQuery = true)
	List<Long> getAdministradoresDeCentro(@Param("idCentro")Long idCentro);

	@Query(value = "SELECT id FROM rolesusuariocentro ORDER BY id DESC LIMIT 1", nativeQuery = true)
	Long getIdMasAltoPermisos();
	
	@Query(value = "SELECT id FROM rolesusuariocentro WHERE usuario = :idUsuario AND centro = :idCentro AND rol = 1 ", nativeQuery = true)
	Long getIdPermisoEmpleado(@Param("idUsuario")Long idEmpleadoAElimininar, @Param("idCentro")Long centroDeAdministrador);
	
	@Query(value = "SELECT COUNT(1) FROM rolesusuariocentro WHERE usuario = :idUsuario AND centro = :idCentro", nativeQuery = true)
	Integer esUsuarioEmpleadoDeCentro(@Param("idUsuario")Long idUsuario, @Param("idCentro")Long idCentro);
	
	@Query(value = "SELECT COUNT(1) FROM rolesusuariocentro WHERE usuario = :idUsuario AND rol = :idRol AND centro = :idCentro", nativeQuery = true)
	Integer tieneUsuarioRolEnCentro(@Param("idUsuario")Long idUsuario, @Param("idRol")Long idRol, @Param("idCentro")Long idCentro);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM rolesusuariocentro WHERE id = :idPermiso", nativeQuery = true)
	void eliminarPermiso(@Param("idPermiso")Long idPermiso);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO rolesusuariocentro (id, usuario, rol, centro) VALUES (:idPermiso, :idUsuario, :idRol, :idCentro)", nativeQuery = true)
	void asignarPermiso(@Param("idPermiso")Long idPermiso, @Param("idUsuario")Long idUsuario, @Param("idRol")Long idRol, @Param("idCentro")Long idCentro);

}
