package webscraper;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


    class Language_data{
        private String nombre_lenguaje;
        private Float rating_github ;
        private int nro_apariciones;

        public Language_data(String nombre_lenguaje, Float rating_github, int nro_apariciones) {
            this.nombre_lenguaje = nombre_lenguaje;
            this.rating_github = rating_github;
            this.nro_apariciones = nro_apariciones;
        }

        //getters
        public String getNombre() { return nombre_lenguaje; }
        public Float getRating() {  return rating_github; }
        public Integer getApariciones() {  return nro_apariciones; }

        //setters
        public void setNombre(String nombre_lenguaje) {  this.nombre_lenguaje = nombre_lenguaje; }
        public void setRating(float rating_github) {  this.rating_github = rating_github;  }
        public void setApariciones(int nro_apariciones) {  this.nro_apariciones = nro_apariciones; }
    }

public class webscraperTema1 {
	
	//para el calculo de rating_github
	public static int max_apariciones = 0;
	public static int min_apariciones = 100000000;


    
	public static void main(String[] args){
                
		List<Language_data> tiobe_languages = new ArrayList<Language_data>();
		cargar_nombres_en_Lista(tiobe_languages);
		
		//conseguir el numero de apariciones de github
		get_nro_apariciones(tiobe_languages);
		
		//guardar los datos en un txt
        guardar_en_txt(tiobe_languages);
		
		//calcular el rating github
		calcular_rating_github(tiobe_languages);
                
		// ordenar el array por rating
		ordenar_por_rating(tiobe_languages);
		
		//imprimir en pantalla
		imprimir_en_pantalla(tiobe_languages);		
		
		//Gráfico ordenado por apariciones
		ordenar_por_apariciones(tiobe_languages);
		mostrar_ventana(tiobe_languages);
	}
	
	public static void cargar_nombres_en_Lista(List<Language_data> tiobe_languages){

		List<String> tiobe_language_names = get_lista_language_names();
		for(String nombre : tiobe_language_names){
			Language_data lenguage = new Language_data(nombre,null,0);
			tiobe_languages.add(lenguage);
		}       
	}
	
	public static void ordenar_por_rating(List<Language_data> tiobe_languages){
		//https://stackoverflow.com/questions/10396970
		Collections.sort(tiobe_languages, new Comparator<Language_data>() {
			@Override
			public int compare(Language_data o1, Language_data o2) {
				return o2.getRating().compareTo(o1.getRating());
			}
		});
	}
	
	public static void ordenar_por_apariciones(List<Language_data> tiobe_languages){
		//https://stackoverflow.com/questions/10396970
		Collections.sort(tiobe_languages, new Comparator<Language_data>() {
			@Override
			public int compare(Language_data o1, Language_data o2) {
				return o2.getApariciones().compareTo(o1.getApariciones());
			}
		});
	}
	
	
	public static void get_nro_apariciones(List<Language_data> tiobe_languages){
		String link_github = "https://github.com/topics/";
		for( int i=0;i<tiobe_languages.size();i++ ){
			try{
				String url = link_github+tiobe_languages.get(i).getNombre();
				System.out.println("Conectando a: "+url);
				Document document = Jsoup.connect( url ).get();
				Element titulo = document.getElementsByClass("h3 color-fg-muted").get(0);
				
				//el numero tiene una coma
				//https://stackoverflow.com/questions/11973383/how-to-parse-number-string-containing-commas-into-an-integer-in-java
				String str_nro_apariciones = titulo.text().split(" ")[2];
				int nro_apariciones = Integer.parseInt(str_nro_apariciones.replaceAll(",", ""));
				
				//verifica si es un maximo o un minimo
				if(max_apariciones<nro_apariciones) max_apariciones = nro_apariciones;
				if(min_apariciones>nro_apariciones) min_apariciones = nro_apariciones;
				
				tiobe_languages.get(i).setApariciones(nro_apariciones);
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	//source: https://www.w3schools.com/java/java_files_create.asp
	public static void guardar_en_txt(List<Language_data> tiobe_languages){
		try {
			FileWriter myWriter = new FileWriter("Resultados.txt");
			for( int i=0;i<tiobe_languages.size();i++ ){
				myWriter.write(tiobe_languages.get(i).getNombre()+","+tiobe_languages.get(i).getApariciones()+"\n");
			}
			myWriter.close();
			System.out.println("Se pudo escribir los datos");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	
	
	public static void calcular_rating_github(List<Language_data> tiobe_languages){
		for( int i=0;i<tiobe_languages.size();i++ ){
			Float rating_github = (((float)tiobe_languages.get(i).getApariciones()-min_apariciones)/(max_apariciones-min_apariciones))*100;
			tiobe_languages.get(i).setRating(rating_github);
		}
	}
	
	public static void imprimir_en_pantalla(List<Language_data> tiobe_languages){
		System.out.printf("%20s%20s%20s","NOMBRE_LENGUAJE","RATING_GITHUB","NRO_APARICIONES\n");
		for( int i=0;i<tiobe_languages.size();i++ ){
			Language_data language = tiobe_languages.get(i);
			System.out.printf("%20s%20s%20s\n",language.getNombre(),language.getRating(),language.getApariciones());
		}
	}
	
	
	public static void mostrar_ventana(List<Language_data> tiobe_languages){
		String titulo = "10 lenguajes con mayor número de apariciones";
		String ejeX = "NOMBRE_LENGUAJE";
		String ejeY = "NRO_APARICIONES";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		//se cargan los datos en el grafico
		for( int i=0;i<tiobe_languages.size();i++ ){
			int numero_apariciones;
			numero_apariciones = tiobe_languages.get(i).getApariciones();
			dataset.setValue(numero_apariciones,"test",tiobe_languages.get(i).getNombre());
		}
		JFreeChart barChart = ChartFactory.createBarChart(
				titulo,ejeX,ejeY,dataset,
				PlotOrientation.VERTICAL,false, true, false);
		
		//source:https://stackoverflow.com/questions/23665260
        JFrame frame = new JFrame("Tabla");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(barChart));
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
	}
        
	public static List get_lista_language_names(){
		List<String> tiobe_language_names = new ArrayList<String>();
		tiobe_language_names.add("python");
		tiobe_language_names.add("c");
		tiobe_language_names.add("java");
		tiobe_language_names.add("cpp");
		tiobe_language_names.add("csharp");
		tiobe_language_names.add("vbnet");
		tiobe_language_names.add("javascript");
		tiobe_language_names.add("assembly-language");
		tiobe_language_names.add("sql");
		tiobe_language_names.add("php");
		tiobe_language_names.add("object-pascal");
		tiobe_language_names.add("swift");
		tiobe_language_names.add("r");
		tiobe_language_names.add("go");
		tiobe_language_names.add("visual basic");
		tiobe_language_names.add("objective-c");
		tiobe_language_names.add("perl");
		tiobe_language_names.add("lua");
		tiobe_language_names.add("ruby");
		tiobe_language_names.add("matlab");
		
		return(tiobe_language_names);
	}
	
	
}
