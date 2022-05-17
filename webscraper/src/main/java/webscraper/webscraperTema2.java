
package webscraper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

    class tag_class{
        private String nombre_tag;
        private int nro_apariciones;
        public tag_class(String nombre_tag, int nro_apariciones) {
            this.nombre_tag = nombre_tag;
            this.nro_apariciones = nro_apariciones;
        }
        //getters
        public String getNombre() { return nombre_tag; }
        public Integer getApariciones() {  return nro_apariciones; }
        //setters
        public void setNombre(String nombre_lenguaje) {  this.nombre_tag = nombre_lenguaje; }
        public void setApariciones(int nro_apariciones) {  this.nro_apariciones = nro_apariciones; }
    }


public class webscraperTema2 {
    public static String tema_de_interes = "bot";

    public webscraperTema2() {
    }
    
	public static void main(String[] args){
		//Map<String,Integer> tags_map = new HashMap<>();
		
		//conseguir las apariciones de github
        int cant_paginas_a_probar=15;
		Map<String,Integer> tags_map = get_tags_relacionadas(cant_paginas_a_probar);
		
		
		//cambio de hashmap a arraylist para poder ordenar
		List<tag_class> lista_tags = new ArrayList<tag_class>();
		for(String nombre: tags_map.keySet()){
			tag_class tag = new tag_class(nombre,tags_map.get(nombre));
			lista_tags.add(tag);
		}
		
		//ordenar en forma descendiente
		ordenar_por_apariciones(lista_tags);
		
		//imprimir los resultados en pantalla
		for( int i=0;i<lista_tags.size();i++ ){
			System.out.println(lista_tags.get(i).getApariciones()+"\t"+lista_tags.get(i).getNombre());
		}
		
		
		//Gráfico ordenado por apariciones
		mostrar_ventana(lista_tags);
	}
	
	
	public static Map<String,Integer> get_tags_relacionadas(int cant_paginas_a_probar){
        // se deja la url incompleta para agregarle despues el nro de pagina
		Map<String,Integer> tags_map = new HashMap<>();
		
		String parametros_url = "?o=desc&s=updated&page=";
		String link_github = "https://github.com/topics/"+tema_de_interes+parametros_url;
		
		for( int i=1;i<=cant_paginas_a_probar;i++ ){
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
					if(actualizado_treinta_dias(fecha_articulo)){
						String tag_class = "topic-tag topic-tag-link f6 mb-2";
						Elements lista_tags_elemento = articulo.getElementsByClass(tag_class);
						for(Element tag : lista_tags_elemento){
							//aca se van a guardar las tags con un contador
							//https://stackoverflow.com/questions/81346/
							tags_map.merge(tag.text(), 1, Integer::sum);
							//si no hay key coloca 1, si ya existe suma 1
						}
					}
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		return (tags_map);
	}
	
	
	public static void ordenar_por_apariciones(List<tag_class> lista_tags){
		//https://stackoverflow.com/questions/10396970
		Collections.sort(lista_tags, new Comparator<tag_class>() {
			@Override
			public int compare(tag_class o1, tag_class o2) {
				return o2.getApariciones().compareTo(o1.getApariciones());
			}
		});
	}
	
	
	public static void mostrar_ventana(List<tag_class> lista_tags){
		String titulo = "20 palabras relacionadas con "+tema_de_interes+" con mayor número de apariciones";
		String ejeX = "TOPIC";
		String ejeY = "NRO_APARICIONES";
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		//si se encontro menos de 20 tags
		int limite =(lista_tags.size()<20)?lista_tags.size():20;
		
		//se cargan los datos en el grafico
		for( int i=0;i<limite;i++ ){
			int numero_apariciones;
			numero_apariciones = lista_tags.get(i).getApariciones();
			dataset.setValue(numero_apariciones,"test",lista_tags.get(i).getNombre());
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

