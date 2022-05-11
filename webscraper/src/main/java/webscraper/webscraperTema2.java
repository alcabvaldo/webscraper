



package webscraper;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
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




public class webscraperTema2 {

    public webscraperTema2() {
    }
    
	public static void main(String[] args){
        String tema_de_interes = "bot";
        // se deja la url incompleta para agregarle despues el nro de pagina
		String link_github = "https://github.com/topics/bot?o=desc&s=updated&page=";
		List<String> tiobe_language_names = new ArrayList<String>();
		List<String> tiobe_language_number = new ArrayList<String>();
		
		
		tiobe_language_names.add("c");
		
		//conseguir las apariciones de github
                int numero=10;
		for( int i=1;i<=numero;i++ ){
                    try{
                        String nro_pagina = Integer.toString(i);
                        System.out.println("conectandose a: "+ link_github+nro_pagina );
                        Document document = Jsoup.connect( link_github+nro_pagina ).get();
						//selecciona articulos
                        Elements lista_articulos = document.getElementsByClass("border rounded color-shadow-small color-bg-subtle my-4");
                        
						//por cada articulo, verifica la fecha, si es correcto guarda los tags
                        for(Element articulo : lista_articulos){
                            Elements elemento_fecha = articulo.select("relative-time");
							String fecha_articulo = elemento_fecha.attr("datetime");
                            //System.out.println("Fecha: "+fecha_articulo);
                            if(actualizado_treinta_dias(fecha_articulo)){
                                //System.out.println("Está actualizado");
								String tag_class = "topic-tag topic-tag-link f6 mb-2";
                                Elements lista_tags = articulo.getElementsByClass(tag_class);
                                for(Element tag : lista_tags){
									//aca se van a guardar las tags con un contador
                                    //System.out.println(tag.text());
                                }
                            }
                        }
                        
                        //String pagina = document.text();//.split(" ")[2];
                        //System.out.println("La pagina es: "+pagina);
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                    }
		}
                
		/*
		//guardar los datos en un txt
                //https://www.w3schools.com/java/java_files_create.asp
                try {
                    FileWriter myWriter = new FileWriter("Resultados.txt");
                    myWriter.write("Files in Java might be tricky, but it is fun enough!");
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
		
		//Gráfico
		String titulo = "10 lenguajes con mayor número de apariciones";
		String ejeX = "NOMBRE_LENGUAJE";
		String ejeY = "NRO_APARICIONES";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                    dataset.setValue(6, "test", "A");
                    dataset.setValue(7, "test", "B");
                    dataset.setValue(15, "test", "C");
		JFreeChart barChart = ChartFactory.createBarChart(
				titulo,ejeX,ejeY,dataset,
				PlotOrientation.VERTICAL,false, true, false);
		mostrar_ventana(barChart);
                
                */
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

    //verifica si la fecha recibida es de hace menos de 30 dias
	//https://stackoverflow.com/questions/20165564/calculating-days-between-two-dates-with-java
	public static boolean actualizado_treinta_dias(String fecha_articulo_str){
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");
            LocalDateTime fecha_actual = LocalDateTime.now();
			LocalDateTime fecha_articulo = LocalDateTime.parse(fecha_articulo_str, dtf);
            //System.out.println("fecha recibida: "+fecha_articulo+"fecha actual: "+fecha_actual);
			long daysBetween = Duration.between(fecha_articulo, fecha_actual).toDays();
            //System.out.println("Days between: "+daysBetween);
			if (daysBetween<=30){
				return(true);
			}else{
				return(false);
			}
	}
}

