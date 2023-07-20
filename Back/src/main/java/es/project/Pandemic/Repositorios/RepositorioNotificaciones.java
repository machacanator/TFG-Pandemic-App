package es.project.Pandemic.Repositorios;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.project.Pandemic.EntidadesYClasesSecundarias.Notificacion;


@Repository
public interface RepositorioNotificaciones extends JpaRepository<Notificacion, Long>{
	
	@Query(value = "SELECT id FROM notificacion ORDER BY id DESC LIMIT 1", nativeQuery = true)
	long getIdMasAlto();
	
	@Query(value = "SELECT * FROM notificacion WHERE id = :idNotificacion", nativeQuery = true)
	Notificacion getNotificacion(@Param("idNotificacion")long idNotificacion);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO notificacion (id, visualizada, codigo, fecha, usuario, idreferencia) VALUES (:idNotificacion, :visualizada, :codigo, :fecha, :idUsuario, :idReferencia)", nativeQuery = true)
	void guardarNuevaNotificacion(@Param("idNotificacion")long idNotificacion, @Param("visualizada")boolean visualizada, @Param("codigo")String codigo, @Param("fecha")Date fecha, @Param("idUsuario")long idUsuario,  @Param("idReferencia")long idReferencia);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM notificacion WHERE id = :idNotificacion", nativeQuery = true)
	void borrarNotificacion(@Param("idNotificacion")long idNotificacion);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE notificacion SET visualizada = true  WHERE id = :idNotificacion", nativeQuery = true)
	void marcarNotificacionVisualizada(@Param("idNotificacion")long idNotificacion);
}
