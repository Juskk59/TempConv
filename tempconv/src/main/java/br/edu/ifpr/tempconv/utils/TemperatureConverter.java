package br.edu.ifpr.tempconv.utils;

import br.edu.ifpr.tempconv.model.types.TemperatureTypes;

public final class TemperatureConverter {
   public static Double celsius2Fahrenheit(Double celsius) {
   		return celsius * 1.8 + 32.;
   }

   public static Double celsius2Kelvin(Double celsius) {
   		return celsius + 273.;
   }

   public static Double fahrenheit2Celsius(Double fahrenheit) {
		return (fahrenheit - 32.) / 1.8;
   }

   public static Double fahrenheit2Kelvin(Double fahrenheit) {
   		return (fahrenheit - 32.) * (5. / 9.) + 273.;
   }

   public static Double kelvin2Celsius(Double kelvin) {
   		return kelvin - 273.;
   }

   public static Double kelvin2Fahrenheit(Double kelvin) {
   		return (kelvin - 273.) * 1.8 + 32.;
   }

	public static Double calculateTempOutput(TemperatureTypes ti,
                                            Double tiv,
                                            TemperatureTypes to) {
		Double result = 0.0;

		switch (ti) {
			case CELSIUS    :
				switch (to) {
					case CELSIUS    :
						result = tiv;
						break;
					case FAHRENHEIT :
						result = TemperatureConverter.celsius2Fahrenheit(tiv);
						break;
					case KELVIN     :
						result = TemperatureConverter.celsius2Kelvin(tiv);
				}
				break;
			case FAHRENHEIT :
				switch (to) {
					case CELSIUS :
						result = TemperatureConverter.fahrenheit2Celsius(tiv);
						break;
					case FAHRENHEIT :
						result = tiv;
						break;
					case KELVIN  :
						result = TemperatureConverter.fahrenheit2Kelvin(tiv);
				}
				break;
			case KELVIN     :
				switch (to) {
					case CELSIUS     :
						result = TemperatureConverter.kelvin2Celsius(tiv);
						break;
					case FAHRENHEIT  :
						result = TemperatureConverter.kelvin2Fahrenheit(tiv);
						break;
					case KELVIN  :
						result = tiv;
					break;
				}
		}
		return result;
	}
}
