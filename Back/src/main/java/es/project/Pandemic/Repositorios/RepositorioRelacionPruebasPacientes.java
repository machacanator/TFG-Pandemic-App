package es.project.Pandemic.Repositorios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.project.Pandemic.EntidadesYClasesSecundarias.RelacionPruebaPaciente;

public interface RepositorioRelacionPruebasPacientes extends JpaRepository<RelacionPruebaPaciente, Long>{
	
	@Query(value = "SELECT id FROM pruebaspacientes ORDER BY id DESC LIMIT 1", nativeQuery = true)
	long getIdMasAltoRelacionPruebasPacientes();
	
	@Query(value = "SELECT COUNT(1) FROM pruebaspacientes WHERE prueba = :idPrueba AND paciente = :idPaciente AND completada = true", nativeQuery = true)
	Integer estaPruebaCompletadaParaPaciente(@Param("idPrueba")long idPrueba, @Param("idPaciente")long idPaciente);
	
	@Query(value = "SELECT prueba FROM pruebaspacientes WHERE paciente = :idPaciente ", nativeQuery = true)
	List<Long> getTodasPruebasPaciente(@Param("idPaciente")long idPaciente);
	
	@Query(value = "SELECT prueba FROM pruebaspacientes WHERE paciente = :idPaciente AND completada = true", nativeQuery = true)
	List<Long> getPruebasCompletadasPaciente(@Param("idPaciente")long idPaciente);
	
	@Query(value = "SELECT prueba FROM pruebaspacientes WHERE paciente = :idPaciente AND completada = false ", nativeQuery = true)
	List<Long> getPruebasIncompletasPaciente(@Param("idPaciente")long idPaciente);
	
	@Query(value = "SELECT prueba FROM pruebaspacientes WHERE paciente = :idPaciente AND completada = true ORDER BY prueba ASC ", nativeQuery = true)
	List<Long> getPruebasRealizadas(@Param("idPaciente")long idPaciente);
	
	@Query(value = "SELECT * FROM pruebaspacientes WHERE paciente = :idPaciente AND prueba = :idPrueba ORDER BY id ASC ", nativeQuery = true)
	RelacionPruebaPaciente getRelacionPruebaPaciente(@Param("idPaciente")long idPaciente, @Param("idPrueba")long idPrueba);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO pruebaspacientes (id, paciente, prueba, completada) VALUES (:id, :idPaciente, :idPrueba, false) ", nativeQuery = true)
	void annadirNuevaPruebaAPaciente(@Param("id")long id, @Param("idPrueba")long idPrueba, @Param("idPaciente")long idPaciente);
}
