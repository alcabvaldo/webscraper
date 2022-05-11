package webscraper;

import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;




public class webscraperTema1 {

    public webscraperTema1() {
    }
    
	public static void main(String[] args){
		String link_github = "https://github.com/topics/";
		List<String> tiobe_language_names = get_lista_language_names();
		List<String> tiobe_language_numbers = new ArrayList<String>();
		
		
		
		
		//conseguir las apariciones de github
		for(String language_name : tiobe_language_names ){
			try{
				Document document = Jsoup.connect( link_github+language_name ).get();
				Element titulo = document.getElementsByClass("h3 color-fg-muted").get(0);
				/**/String num_apariciones = titulo.text().split(" ")[2];
				tiobe_language_numbers.add(titulo.text().split(" ")[2]);
				System.out.println("El numero de apariciones de "+language_name+" es: "+num_apariciones);
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		System.out.println("La cantidad de elementos es: "+tiobe_language_names.size());
		
		//guardar los datos en un txt
        //https://www.w3schools.com/java/java_files_create.asp
		try {
			FileWriter myWriter = new FileWriter("Resultados.txt");
			for( int i=0;i<tiobe_language_names.size();i++ ){
				myWriter.write(tiobe_language_names.get(i)+","+tiobe_language_numbers.get(i));
			}
			myWriter.close();
			System.out.println("Se pudo escribir los datos");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		
		//Gráfico
		String titulo = "10 lenguajes con mayor número de apariciones";
		String ejeX = "NOMBRE_LENGUAJE";
		String ejeY = "NRO_APARICIONES";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		//se cargan los datos en el grafico
		for( int i=0;i<tiobe_language_names.size();i++ ){
			//este por el formato del numero que tiene coma para los miles
			//https://stackoverflow.com/questions/11973383/
			Number numero_apariciones = NumberFormat.getNumberInstance(java.util.Locale.US).parse(tiobe_language_numbers.get(i));
			dataset.setValue(numero_apariciones,"test",tiobe_language_names.get(i));
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
