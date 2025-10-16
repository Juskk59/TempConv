package br.edu.ifpr.tempconv.model.types;

public enum TemperatureTypes {
   CELSIUS(1,"Celsius",'\u2103'),       // \u2103 => °C
   FAHRENHEIT(2,"Fahrenheit",'\u2109'), // \u2109 => °F
   KELVIN(3,"Kelvin",'\u212A');         // \u212A => °K

   private int    index;
   private String description;
   private char   symbol;

   TemperatureTypes(int index, String description, char symbol) {
      this.index        = index;
      this.description  = description;
      this.symbol       = symbol;
   }

   public int index() { return index; }
   public String description() { return description; }
   public char symbol() { return symbol; }

//   public TemperatureTypes fromIndex(int index) {
//   		for (TemperatureTypes tt : values())
//   			if (tt.index() == index)
//   				return tt;
//
//   		throw new IllegalArgumentException("(1) Índice inválido: " + index);
//   }

//   public static TemperatureTypes fromString(String param) {
//   		try {
//   			int valor = Integer.parseInt(param);
//
//   			for (TemperatureTypes tt : values())
//   				if (tt.index() == valor)
//   					return tt;
//   		}
//   		catch (NumberFormatException e) {
//   			throw new IllegalArgumentException("(2.1) Índice inválido: " + param);
//   		}
//   		throw new IllegalArgumentException("(2.2)  Índice inválido: " + param);
//   }
}
