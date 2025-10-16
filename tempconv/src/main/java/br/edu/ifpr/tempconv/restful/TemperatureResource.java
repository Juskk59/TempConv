package br.edu.ifpr.tempconv.restful;

import br.edu.ifpr.tempconv.model.Temperature;
import br.edu.ifpr.tempconv.model.types.TemperatureTypes;
import br.edu.ifpr.tempconv.repository.TemperatureRepository;
import br.edu.ifpr.tempconv.utils.TemperatureConverter;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Path("/temperatures")
public class TemperatureResource {

    @Inject
    private TemperatureRepository repository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTemperature(Temperature temp) {
        if (temp.getTimestamp() == null) {
            temp.setTimestamp(LocalDateTime.now());
        }
        // O ID será gerado no repositório se for nulo
        Double tempo = TemperatureConverter.calculateTempOutput(temp.getTypei(), temp.getTempi(), temp.getTypeo());
        temp.setTempo(tempo);
        Temperature savedTemp = repository.save(temp);
        return Response.status(Response.Status.CREATED).entity(savedTemp).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTemperature(@PathParam("id") Long id, Temperature tempUpdate) {
        Optional<Temperature> optionalTemp = repository.findById(id);
        if (optionalTemp.isPresent()) {
            Temperature temp = optionalTemp.get();
            boolean needsRecalculation = false;

            if (tempUpdate.getTypei() != null && !tempUpdate.getTypei().equals(temp.getTypei())) {
                temp.setTypei(tempUpdate.getTypei());
                needsRecalculation = true;
            }
            if (tempUpdate.getTypeo() != null && !tempUpdate.getTypeo().equals(temp.getTypeo())) {
                temp.setTypeo(tempUpdate.getTypeo());
                needsRecalculation = true;
            }
            if (tempUpdate.getTempi() != null && !tempUpdate.getTempi().equals(temp.getTempi())) {
                temp.setTempi(tempUpdate.getTempi());
                needsRecalculation = true;
            }

            if (needsRecalculation) {
                Double tempo = TemperatureConverter.calculateTempOutput(temp.getTypei(), temp.getTempi(), temp.getTypeo());
                temp.setTempo(tempo);
            }
            temp.setId(id); // Garante que o ID está correto para a atualização
            if (repository.update(temp)) {
                return Response.ok(temp).build();
            } else {
                return Response.status(Response.Status.NOT_MODIFIED).build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTemperatureById(@PathParam("id") Long id) {
        if (repository.deleteById(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/byTimestamp")
    public Response deleteTemperatureByTimestamp(@QueryParam("timestamp") Long timestamp) {
        if (timestamp == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Timestamp parameter is required.").build();
        }
        // Convert milliseconds to LocalDateTime for comparison if needed, or pass directly to repository
        // For simplicity, let's assume repository can handle Long timestamp directly or convert it.
        if (repository.deleteByTimestamp(timestamp)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    public Response deleteAllTemperatures() {
        int deletedCount = repository.deleteAll();
        return Response.ok("Total de " + deletedCount + " temperaturas excluídas.").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTemperatures(@QueryParam("id") Long id,
                                    @QueryParam("tempi") Double tempi,
                                    @QueryParam("typei") TemperatureTypes typei,
                                    @QueryParam("tempo") Double tempo,
                                    @QueryParam("typeo") TemperatureTypes typeo) {
        if (id != null) {
            Optional<Temperature> temp = repository.findById(id);
            if (temp.isPresent()) {
                return Response.ok(temp.get()).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

        List<Temperature> temps = repository.findByAttributes(tempi, typei, tempo, typeo);
        if (temps.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(temps).build();
        }
    }
}

