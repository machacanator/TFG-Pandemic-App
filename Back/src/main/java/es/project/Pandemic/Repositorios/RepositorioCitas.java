package es.project.Pandemic.Repositorios;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.project.Pandemic.EntidadesYClasesSecundarias.Cita;


@Repository
public interface RepositorioCitas extends JpaRepository<Cita, Long>{
	
	@Query(value = "SELECT id FROM cita ORDER BY id DESC LIMIT 1", nativeQuery = true)
	long getIdMasAlto();
	
	@Query(value = "SELECT * FROM cita WHERE id = :idCita", nativeQuery = true)
	Cita getCitaPorId(@Param("idCita")long idCita);
	
	@Query(value = "SELECT COUNT(1) FROM cita WHERE \"prueba complementaria\" = :idPrueba AND estado = 0 AND paciente = :idPaciente", nativeQuery = true)
	Integer existeCitaPendiente(@Param("idPrueba")long idPrueba, @Param("idPaciente")long idPaciente);
	
	@Query(value = "SELECT COUNT(1) FROM cita WHERE \"prueba complementaria\" = :idPrueba AND centro = :idCentro AND fecha = :fechaCita", nativeQuery = true)
	Integer existeCita(@Param("idPrueba")long idPrueba, @Param("idCentro")long idCentro, @Param("fechaCita")Timestamp fechaCita);
	
	@Query(value = "SELECT EXTRACT(DAY FROM fecha) AS dia, COUNT(*) AS total FROM cita WHERE EXTRACT(MONTH FROM DATE(fecha)) = :mes AND EXTRACT(YEAR FROM DATE(fecha)) = :anno AND \"prueba complementaria\" = :idPrueba AND centro = :idCentro GROUP BY dia;", nativeQuery = true)
	List<Object[]> getRecuentoCitasMensualParaPruebaEnCentro(@Param("mes")int mes, @Param("anno")int anno, @Param("idPrueba")Long idPrueba, @Param("idCentro")Long idCentro);
	
	@Query(value = "SELECT EXTRACT(MONTH FROM fecha) AS mes, COUNT(*) AS total FROM cita WHERE EXTRACT(YEAR FROM DATE(fecha)) = :anno AND \"prueba complementaria\" = :idPrueba AND centro = :idCentro GROUP BY mes;", nativeQuery = true)
	List<Object[]> getRecuentoCitasAnualParaPruebaEnCentro(@Param("anno")int anno, @Param("idPrueba")Long idPrueba, @Param("idCentro")Long idCentro);
	
	@Query(value = "SELECT EXTRACT(DAY FROM fecha) AS dia, COUNT(*) AS total FROM cita WHERE EXTRACT(MONTH FROM DATE(fecha)) = :mes AND EXTRACT(YEAR FROM DATE(fecha)) = :anno AND centro = :idCentro AND enfermero = :idUsuario GROUP BY dia;", nativeQuery = true)
	List<Object[]> getRecuentoCitasMensualParaUsuarioEnCentro(@Param("mes")int mes, @Param("anno")int anno, @Param("idCentro")Long idCentro, @Param("idUsuario")long idUsuario);
	
	@Query(value = "SELECT EXTRACT(MONTH FROM fecha) AS mes, COUNT(*) AS total FROM cita WHERE EXTRACT(YEAR FROM DATE(fecha)) = :anno AND centro = :idCentro AND enfermero = :idUsuario GROUP BY mes;", nativeQuery = true)
	List<Object[]> getRecuentoCitasAnualParaUsuarioEnCentro(@Param("anno")int anno, @Param("idCentro")Long idCentro, @Param("idUsuario")long idUsuario);
	
	@Query(value = "SELECT EXTRACT(DAY FROM fecha) AS dia, COUNT(*) AS total FROM cita WHERE EXTRACT(MONTH FROM DATE(fecha)) = :mes AND EXTRACT(YEAR FROM DATE(fecha)) = :anno AND centro = :idCentro GROUP BY dia;", nativeQuery = true)
	List<Object[]> getRecuentoCitasMensualDeCentro(@Param("idCentro")Long idCentro, @Param("mes")int mes, @Param("anno")int anno);
	
	@Query(value = "SELECT EXTRACT(MONTH FROM fecha) AS mes, COUNT(*) AS total FROM cita WHERE EXTRACT(YEAR FROM DATE(fecha)) = :anno AND centro = :idCentro GROUP BY mes;", nativeQuery = true)
	List<Object[]> getRecuentoCitasAnualDeCentro(@Param("idCentro")Long idCentro, @Param("anno")int anno);
	
	@Query(value = "SELECT EXTRACT(DAY FROM fecha) AS dia, COUNT(*) AS total FROM cita WHERE EXTRACT(MONTH FROM DATE(fecha)) = :mes AND EXTRACT(YEAR FROM DATE(fecha)) = :anno AND \"prueba complementaria\" = :idPrueba AND centro = :idCentro GROUP BY dia;", nativeQuery = true)
	List<Object[]> getRecuentoCitasMensualDeCentroParaPrueba(@Param("idCentro")Long idCentro, @Param("idPrueba")Long idPrueba, @Param("mes")int mes, @Param("anno")int anno);
	
	@Query(value = "SELECT EXTRACT(MONTH FROM fecha) AS mes, COUNT(*) AS total FROM cita WHERE EXTRACT(YEAR FROM DATE(fecha)) = :anno AND \"prueba complementaria\" = :idPrueba AND centro = :idCentro GROUP BY mes;", nativeQuery = true)
	List<Object[]> getRecuentoCitasAnualDeCentroParaPrueba(@Param("idCentro")Long idCentro, @Param("idPrueba")Long idPrueba, @Param("anno")int anno);
	
	@Query(value = "SELECT COUNT(*) AS total FROM cita WHERE EXTRACT(MONTH FROM DATE(fecha)) = :mes AND EXTRACT(YEAR FROM DATE(fecha)) = :anno AND \"prueba complementaria\" = :idPrueba AND centro = :idCentro ;", nativeQuery = true)
	Long getRecuentoPruebaEnCentroMensual(@Param("idPrueba")Long idPrueba, @Param("idCentro")Long idCentro, @Param("mes")int mes, @Param("anno")int anno);
	
	@Query(value = "SELECT COUNT(*) AS total FROM cita WHERE EXTRACT(YEAR FROM DATE(fecha)) = :anno AND \"prueba complementaria\" = :idPrueba AND centro = :idCentro ;", nativeQuery = true)
	Long getRecuentoPruebaEnCentroAnual(@Param("idPrueba")Long idPrueba, @Param("idCentro")Long idCentro, @Param("anno")int anno);
	
	@Query(value = "SELECT COUNT(*) AS total FROM cita WHERE EXTRACT(MONTH FROM DATE(fecha)) = :mes AND EXTRACT(YEAR FROM DATE(fecha)) = :anno AND \"prueba complementaria\" = :idPrueba AND centro = :idCentro AND enfermero = :idUsuario ;", nativeQuery = true)
	Long getRecuentoPruebaEnCentroMensualParaUsuario(@Param("idPrueba")Long idPrueba, @Param("idCentro")Long idCentro, @Param("mes")int mes, @Param("anno")int anno, @Param("idUsuario")long idUsuario);
	
	@Query(value = "SELECT COUNT(*) AS total FROM cita WHERE EXTRACT(YEAR FROM DATE(fecha)) = :anno AND \"prueba complementaria\" = :idPrueba AND centro = :idCentro AND enfermero = :idUsuario ;", nativeQuery = true)
	Long getRecuentoPruebaEnCentroAnualParaUsuario(@Param("idPrueba")Long idPrueba, @Param("idCentro")Long idCentro, @Param("anno")int anno, @Param("idUsuario")long idUsuario);
	
	@Query(value = "SELECT fecha FROM cita WHERE \"prueba complementaria\" = :idPrueba AND paciente = :idPaciente AND estado = 1 ORDER BY fecha ASC", nativeQuery = true)
	List<Date> getFechaUltimaCitaDePruebaParaPaciente(@Param("idPrueba")long idPrueba, @Param("idPaciente")long idPaciente);
	
	@Query(value = "SELECT cast(fecha as time) FROM cita WHERE \"prueba complementaria\" = :idPrueba AND centro = :idCentro AND DATE(fecha) = DATE(:fecha) AND estado = 0 ORDER BY fecha", nativeQuery = true)
	List<Time> getCitasFecha(@Param("idPrueba")long idPrueba, @Param("idCentro")long idCentro, @Param("fecha")Date fecha);
	
	@Query(value = "SELECT * FROM cita WHERE \"prueba complementaria\" = :idPrueba AND centro = :idCentro AND DATE(fecha) = DATE(:fecha) ORDER BY fecha DESC", nativeQuery = true)
	List<Object[]> getCitasDiario(@Param("idPrueba")long idPrueba, @Param("idCentro")long idCentro, @Param("fecha")Date fecha);
	
	@Query(value = "SELECT * FROM cita WHERE paciente = :idPaciente AND estado = 0 ORDER BY fecha DESC", nativeQuery = true)
	List<Object[]> getCitasPendientesPaciente(@Param("idPaciente")long idPaciente);
	
	@Query(value = "SELECT * FROM cita WHERE paciente = :idPaciente AND estado != 0 ORDER BY fecha DESC", nativeQuery = true)
	List<Object[]> getHistoricoPaciente(@Param("idPaciente")long idPaciente);
	
	//@Query(value = "SELECT * FROM cita WHERE enfermero = :idEnfermero ORDER BY fecha DESC;", nativeQuery = true)
	@Query(value = "SELECT cita.id, paciente, nombre, apellidos, \"prueba complementaria\", estado, fecha, centro, observaciones, codigoqr, enfermero FROM cita INNER JOIN persona ON cita.paciente = persona.id  WHERE enfermero = :idEnfermero ORDER BY fecha DESC;", nativeQuery = true)
	List<Object[]> getHistoricoCitasEnfermero(@Param("idEnfermero")long idEnfermero);
	
	//@Query(value = "SELECT * FROM cita WHERE centro = :idCentro AND estado != 0 ORDER BY fecha DESC", nativeQuery = true)
	@Query(value = "SELECT cita.id, paciente, nombre, apellidos, \"prueba complementaria\", estado, fecha, centro, observaciones, codigoqr, enfermero FROM cita INNER JOIN persona ON cita.paciente = persona.id  WHERE centro = :idCentro AND estado != 0 ORDER BY fecha DESC;", nativeQuery = true)
	List<Object[]> getHistoricoCitasCentro(@Param("idCentro")long idCentro);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO cita (id, paciente, \"prueba complementaria\", estado, fecha, centro, observaciones, codigoqr, enfermero) VALUES (:idCita, :idPaciente, :idPrueba, 0, :fecha, :idCentro, null, null, :idEnfermero);", nativeQuery = true)
	void guardarNuevaCita(@Param("idCita")Long idCita, @Param("idPaciente")Long idPaciente, @Param("idPrueba")Long idPrueba, @Param("fecha")Date fecha, @Param("idCentro")Long idCentro, @Param("idEnfermero")Long idEnfermero);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE cita SET paciente = :idPaciente, \"prueba complementaria\" = :idPrueba, fecha = :fecha, centro = :idCentro, enfermero = :idEnfermero WHERE id = :idCita", nativeQuery = true)
	void actualizarCita(@Param("idCita")Long idCita, @Param("idPaciente")Long idPaciente, @Param("idPrueba")Long idPrueba, @Param("fecha")Date fecha, @Param("idCentro")Long idCentro, @Param("idEnfermero")Long idEnfermero);
}
