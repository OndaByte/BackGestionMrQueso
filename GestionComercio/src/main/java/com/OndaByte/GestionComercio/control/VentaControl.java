package com.OndaByte.GestionComercio.control;

import com.OndaByte.GestionComercio.DAO.DAOProducto;
import com.OndaByte.GestionComercio.modelo.ItemVenta;
import com.OndaByte.GestionComercio.modelo.Producto;
import com.OndaByte.GestionComercio.modelo.Venta;
import com.OndaByte.GestionComercio.util.Seguridad;

import java.util.ArrayList;
import java.util.List;

import javax.print.PrintService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.jetty.http.HttpStatus;



public class VentaControl {
/*

import spark.Request;
import spark.Response;
import spark.Route;
  
  
	//    @PostMapping(value = "Venta")

	private static ObjectMapper objectMapper = new ObjectMapper();

	public static Route alta = (Request req, Response res) -> {
		Venta nueva = objectMapper.readValue(req.body(), Venta.class);
		controlesVenta(nueva);

        Caja actual = cajadao.getCaja();
        System.out.println("paso controles");
        n.setCaja(actual);
        actual.sumarTotal(n.getTotal());
        System.out.println("sumo total a la caja");
        n = dao.nuevaVenta(n);
        System.out.println("creo la venta");
        proddao.altaProductosVenta(n);
        
        System.out.println("dio de alta los items, procedo a imprimir comanda");
        printComanda(n);
        
        System.out.println("imprimo otra comanda");
        printComanda(n);
        return ResponseEntity.status(HttpStatus.CREATED).body("Venta subida con exito.\n");
    }


    public static Route modificar = (Request req, Response res) -> {
        DAOProducto dao = new DAOProducto();
        String id = req.params(":id");
        Producto nuevo = objectMapper.readValue(req.body(), Producto.class);
        nuevo.setId(Integer.parseInt(id));
        if (dao.modificar(nuevo)){
            res.status(201);
            return "Actualizacion exitosa";
        }
        res.status(500);
        return "Error al actualizar el producto";
    };




    public String getLines(String x){
        String[] arr = x.split(" ");
        String aux = "";
        String line = "";
        for(int i = 0; i<arr.length; i++){
            if(arr[i].length()+line.length()>32){
                aux +=line+"\n";
                line = arr[i] +" ";
            }else
                line += arr[i]+" ";
        }
        aux += line;
        return aux;
    }
    public void printComanda(Ventas venta){
        //Preparamos las variables
        try {
            //PrintService printService = PrinterOutputStream.getDefaultPrintService();
            //PrintService printService = PrinterOutputStream.getPrintServiceByName("Microsoft XPS Document Writer");
            PrintService printService = PrinterOutputStream.getPrintServiceByName("POS-58");
            //System.out.println("Obtuve el tipo");
            PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream);
            PrintModeStyle normal = new PrintModeStyle();
            //Definimos los estilos
            PrintModeStyle title = new PrintModeStyle()
                    .setFontSize(true, true)
                    .setJustification(EscPosConst.Justification.Center);
            PrintModeStyle NombreStyle = new PrintModeStyle()
                    .setFontSize(true, false)
                    .setJustification(EscPosConst.Justification.Center);
            PrintModeStyle Rigth = new PrintModeStyle()
                    .setFontSize(false, true)
                .setJustification(EscPosConst.Justification.Right);
            PrintModeStyle Left = new PrintModeStyle()
                    .setFontSize(false, false);

            PrintModeStyle subtitle = new PrintModeStyle()
                    .setFontSize(false, true)
                    .setBold(true);

            PrintModeStyle bold = new PrintModeStyle();
            //Imrpmimos el logo
            // specify the algorithm that defines what and how "print or not print" on each coordinate of the BufferedImage.
             // in this case, threshold 127
            Bitonal algorithm = new BitonalThreshold(127);

            // this wrapper uses esc/pos sequence: "ESC '*'"
            BitImageWrapper imageWrapper = new BitImageWrapper();
            //centrada
//            imageWrapper.setJustification(EscPosConst.Justification.Center);
            //escpos.write(imageWrapper, escposImage);
            escpos.writeLF(title,venta.getTipo());
            escpos.writeLF(subtitle,"Domicilio: ")
                .writeLF(subtitle,this.getLines(venta.getDomicilio()));
                
            escpos.writeLF(normal,"--------------------------------"); 
            // imprimimos los platos
            for (ItemsVenta p : venta.getItems()) {
                //p.getCantidad();
                Productos producto = proddao.getProducto(p.getProducto_id());
                
                String Cantidad = String.valueOf(p.getCantidad());
                String nombre = producto.getNombre();
                String subtotal = "$ "+String.valueOf(p.getSubtotal());
                escpos.writeLF(normal,this.getLines(nombre));
                escpos.writeLF(normal,"Cant: "+Cantidad+spaces(Cantidad.length()+6,(String.valueOf(subtotal)).length())+subtotal);
            }
            float vuelto = 0f;
            if(venta.getPago()>venta.getTotal()){
                vuelto = venta.getPago()-venta.getTotal();
            }
            //Imprimimos el total
            String vueltoStr = " Vuelto: "+vuelto;
            escpos.writeLF(normal,"--------------------------------")
                .writeLF(bold,(" Hora: "+venta.getCreated_at()))
                .writeLF(bold,(" metodo pago: "+venta.getMetodo_pago()))
                .writeLF(bold,(" Total: "+venta.getTotal()))
                .writeLF(bold,(" Pago: "+venta.getPago()))
                .writeLF(bold,vueltoStr)
                .writeLF(normal,"--------------------------------")
                .writeLF(normal,"")
                .writeLF(normal,"");
            //img=ImageIO.read(new File(ruta+"qr.png"));
            //escposImage = new EscPosImage(new CoffeeImageImpl(img), algorithm);
            imageWrapper.setJustification(EscPosConst.Justification.Center);
            //escpos.write(imageWrapper, escposImage).feed(3);
            escpos.close();
        } catch (Exception e) {
               //En caso de excepcion
            System.out.println(e);
               // deberiamos devolverle el ticket y avisar para que lo haga a mano
               // buscar ideas de mecanismo robusto para esto.
        }

    }
    public String spaces(int tamCant,int tamSubtotal){
        String l = "";
        int cant = 32 -tamCant - tamSubtotal -2;
        for (int i = 0; i < cant; i++) {
            l+=" ";
        }
        return l;
    }

    public static void controlesVenta(Venta n) throws Exception {
        if(n.getItems() == null || n.getItems().isEmpty())
            throw new IllegalArgumentException("La venta debe tener productos.\n");
        List<ItemVenta> itemsAux = new ArrayList<>();
        itemsAux.addAll(n.getItems());
        controlStock(itemsAux);
//        controlTotal(itemsAux,n.getTotal());
    }

    public static void controlStock(List<ItemVenta> prods) throws Exception {
        Producto aux;
		DAOProducto proddao = new DAOProducto();
        for(ItemVenta p : prods){
            aux = proddao.filtrar(p.getProducto_id()+"");
            if(aux.getStock_actual() - p.getCantidad() < 0) {
                throw new IllegalArgumentException("Se excedio el stock del producto '"+aux.getNombre()+"'.\n");
            }
        }
    }	*/
}
