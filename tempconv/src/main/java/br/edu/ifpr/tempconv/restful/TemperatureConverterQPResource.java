package br.edu.ifpr.tempconv.restful;

import br.edu.ifpr.tempconv.model.Temperature;
import br.edu.ifpr.tempconv.model.types.TemperatureTypes;
import br.edu.ifpr.tempconv.utils.TemperatureConverter;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("converterqp")
public class TemperatureConverterQPResource {
	@GET @Path("c2fqp2")
	public Response c2fpp(@QueryParam("ti") TemperatureTypes ti,
				             @QueryParam("tiv") Double tiv,
				             @QueryParam("to") TemperatureTypes to) {
	   Double tempo = TemperatureConverter.calculateTempOutput(ti,tiv,to);

	   Temperature temp  = new Temperature(tiv,ti,tempo,to);

      return Response.ok()
		               .entity(temp.toString())
		               .header("Content-Type",MediaType.TEXT_PLAIN +
		                                      ";charset=utf-8")
		               .build();
	}

	@GET @Path("c2fqp")
	public Response c2fqp(@QueryParam("ti") Double tempi) {
		Double tempo = TemperatureConverter.celsius2Fahrenheit(tempi);

		return Response.ok().entity(tempo).build();
	}
	@GET @Path("c2kqp")
	public Response c2kqp(@QueryParam("ti") Double tempi) {
		Double tempo = TemperatureConverter.celsius2Kelvin(tempi);

		return Response.ok().entity(tempo).build();
	}

	@GET @Path("f2cqp")
	public Response f2cqp(@QueryParam("ti") Double tempi) {
		Double tempo = TemperatureConverter.fahrenheit2Celsius(tempi);

		return Response.ok().entity(tempo).build();
	}
	@GET @Path("f2kqp")
	public Response f2kqp(@QueryParam("ti") Double tempi) {
		Double tempo = TemperatureConverter.fahrenheit2Kelvin(tempi);

		return Response.ok().entity(tempo).build();
	}

	@GET @Path("k2cqp")
	public Response k2cqp(@QueryParam("ti") Double tempi) {
		Double tempo = TemperatureConverter.kelvin2Celsius(tempi);

		return Response.ok().entity(tempo).build();
	}
	@GET @Path("k2fqp")
	public Response k2fqp(@QueryParam("ti") Double tempi) {
		Double tempo = TemperatureConverter.kelvin2Fahrenheit(tempi);

		return Response.ok().entity(tempo).build();
	}
}
