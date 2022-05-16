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
        public int getApariciones() {  return nro_apariciones; }

        //setters
        public void setNombre(String nombre_lenguaje) {  this.nombre_lenguaje = nombre_lenguaje; }
        public void setRating(float rating_github) {  this.rating_github = rating_github;  }
        public void setApariciones(int nro_apariciones) {  this.nro_apariciones = nro_apariciones; }
    }

public class webscraperTema1 {

    public webscraperTema1() {
    }
    
        //Arraylist<Tuple> arr=new Arraylist<Tuple>();
        //Tuple t=new Tuple(double, int, int);


    
	public static void main(String[] args){
		String link_github = "https://github.com/topics/";
                
                List<Language_data> tiobe_languages = new ArrayList<Language_data>();
                
                cargar_nombres_en_Lista(tiobe_languages);
                
		List<String> tiobe_language_names = get_lista_language_names();
		List<String> tiobe_language_numbers = new ArrayList<String>();
		
		
		
		
		//conseguir el numero de apariciones de github
		for(String language_name : tiobe_language_names ){
			try{
				Document document = Jsoup.connect( link_github+language_name ).get();
				Element titulo = document.getElementsByClass("h3 color-fg-muted").get(0);
				/**///String num_apariciones = titulo.text().split(" ")[2];
				tiobe_language_numbers.add(titulo.text().split(" ")[2]);
                                tiobe_languages
				//System.out.println("El numero de apariciones de "+language_name+" es: "+num_apariciones);
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		
		//guardar los datos en un txt
                //https://www.w3schools.com/java/java_files_create.asp
		try {
			FileWriter myWriter = new FileWriter("Resultados.txt");
			for( int i=0;i<tiobe_language_names.size();i++ ){
				myWriter.write(tiobe_language_names.get(i)+","+tiobe_language_numbers.get(i)+"\n");
			}
			myWriter.close();
			System.out.println("Se pudo escribir los datos");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
                
                
                // ordenar el array por rating
                Collections.sort(tiobe_languages, new Comparator<Language_data>() {
                    @Override
                    public int compare(Language_data o1, Language_data o2) {
                        return o2.getRating().compareTo(o1.getRating());
                    }
                });
                
                //imprimir en pantalla
                //
                //
                //
                //
		
		
		//Gráfico
		String titulo = "10 lenguajes con mayor número de apariciones";
		String ejeX = "NOMBRE_LENGUAJE";
		String ejeY = "NRO_APARICIONES";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		//se cargan los datos en el grafico
		for( int i=0;i<tiobe_language_names.size();i++ ){
			
			//este por el formato del numero que tiene coma para los miles
			//https://stackoverflow.com/questions/11973383/
			Number numero_apariciones;
			try {
				numero_apariciones = NumberFormat.getNumberInstance(java.util.Locale.US).parse(tiobe_language_numbers.get(i));
				dataset.setValue(numero_apariciones,"test",tiobe_language_names.get(i));
			} catch (ParseException ex) {
				Logger.getLogger(webscraperTema1.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		JFreeChart barChart = ChartFactory.createBarChart(
				titulo,ejeX,ejeY,dataset,
				PlotOrientation.VERTICAL,false, true, false);
		mostrar_ventana(barChart);
	}
	
	
	//source:https://stackoverflow.com/questions/23665260
	public static void mostrar_ventana(JFreeChart chart){
		
        JFrame frame = new JFrame("Tabla");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
	}
	
        public static void cargar_nombres_en_Lista(List<Language_data> tiobe_languages){
            
            List<String> tiobe_language_names = get_lista_language_names();
            for(String nombre : tiobe_language_names){
                Language_data lenguage = new Language_data(nombre,null,0);
                tiobe_languages.add(lenguage);
            }
            
	}
        
	public static List get_lista_language_names(){
		List<String> tiobe_language_names = new ArrayList<String>();
		tiobe_language_names.add("matlab");
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
		
		return(tiobe_language_names);
	}
	
	
}
