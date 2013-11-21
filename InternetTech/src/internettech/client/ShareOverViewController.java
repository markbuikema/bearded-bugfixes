package internettech.client;

import internettech.model.UserAccount;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;

public class ShareOverViewController implements Initializable {

    private BarChart<String, Integer> shareBarChart;
    private UserAccount acc; 
    private PrintWriter out;
    private BufferedReader in;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initBarchart();
    }

    public ShareOverViewController(UserAccount acc, PrintWriter out, BufferedReader in) {
        this.acc = acc;
        this.out = out;
        this.in = in;
    }

    private void initBarchart() {
        
    }

}
