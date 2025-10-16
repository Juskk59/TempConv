package br.edu.ifpr.tempconv.restful;

import br.edu.ifpr.tempconv.model.Temperature;
import br.edu.ifpr.tempconv.model.types.TemperatureTypes;
import br.edu.ifpr.tempconv.utils.TemperatureConverter;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("converterpp")
public class TemperatureConverterPPResource {
	@GET @Path("c2fpp/{ti}/{tiv}/{to}")
	public Response c2fpp(@PathParam("ti") TemperatureTypes ti,
				             @PathParam("tiv") Double tiv,
				             @PathParam("to") TemperatureTypes to) {
	   Double tempo = TemperatureConverter.calculateTempOutput(ti,tiv,to);

	   Temperature temp  = new Temperature(tiv,ti,tempo,to);

      return Response.ok(ti)
		               .entity(temp.toString())
		               .header("Content-Type",MediaType.TEXT_PLAIN +
		                                      ";charset=utf-8")
		               .build();
	}

	@GET @Path("c2fpp/{tempi:[-+]?\\d*([.,]\\d+)?}")
	// Expressão regular para aceitar números de ponto flutuante com ponto ou
	// vírgula como separador de decimais
	/*
	^[-+]?\d*([.,]\d+)?$

   ^: marca o início da string

   [-+]?: sinal numérico (mais ou menos) optional (?)

   \d*: um ou mais (*) dígitos antes do separador de decimais, p.ex. .5 or ,5

   ([.,]\d+)?: os parênteses marcam um grupo opcional (?) que processa a parte
                decimal do número
   
       [.,]: corresponde ao ponto ou vírgula como separador de decimais

       \d+: corresponde a um ou mais (+) dígitos na parte decimal, garante
             que se houver o separador decimal, há pelo menos um dígito após ele

   $: marca o fim da string
   */
	public Response c2fpp(@PathParam("tempi") String temperature) {
	   String str = temperature.replace(',','.');

	   Double      tempi = Double.valueOf(str);
	   Double      tempo = TemperatureConverter.celsius2Fahrenheit(tempi);
      Temperature temp  = new Temperature(tempi,TemperatureTypes.CELSIUS,
                                          tempo,TemperatureTypes.FAHRENHEIT);

      return Response.ok()
		               .entity(temp.toString())
		               .header("Content-Type",MediaType.TEXT_PLAIN +
		                                      ";charset=utf-8")
		               .build();
	}
	@GET @Path("c2kpp/{tempi}")
	public Response c2kpp(@PathParam("tempi") Double tempi) {
		Double tempo = TemperatureConverter.celsius2Kelvin(tempi);

		return Response.ok().entity(tempo).build();
	}

	@GET @Path("f2cpp/{tempi}")
	public Response f2cpp(@PathParam("tempi") Double tempi) {
		Double tempo = TemperatureConverter.fahrenheit2Celsius(tempi);

		return Response.ok().entity(tempo).build();
	}
	@GET @Path("f2kpp/{tempi}")
	public Response f2kpp(@PathParam("tempi") Double tempi) {
		Double tempo = TemperatureConverter.fahrenheit2Kelvin(tempi);

		return Response.ok().entity(tempo).build();
	}

	@GET @Path("k2cpp/{tempi}")
	public Response k2cpp(@PathParam("tempi") Double tempi) {
		Double tempo = TemperatureConverter.kelvin2Celsius(tempi);

		return Response.ok().entity(tempo).build();
	}
	@GET @Path("k2fpp/{tempi}")
	public Response k2fpp(@PathParam("tempi") Double tempi) {
		Double tempo = TemperatureConverter.kelvin2Fahrenheit(tempi);

		return Response.ok().entity(tempo).build();
	}
}
