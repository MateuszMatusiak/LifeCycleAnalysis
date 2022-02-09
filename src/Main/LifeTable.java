package Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class LifeTable implements Initializable {

    ArrayList<Data> data = new ArrayList<>();
    ArrayList<LifeTableData> lfData = new ArrayList<>();
    int interval;
    int numberOfDays;

    @FXML
    private VBox table;

    @FXML
    private VBox charts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        interval = Controller.publicInterval;
        numberOfDays = Controller.publicNumberOfDays;
        for (int i = 0; i < Controller.data.size(); ++i) {
            data.add(new Data(Controller.data.get(i)));
        }
        Collections.sort(data);
        computeTimeIntervals();
        computeN();
        computeD();
        computeW();
        computeSurvival();
        computeMidpoint();
        computeHazard();
        setTable();
        setLineChart();
    }

    private void computeTimeIntervals() {
        int loop = (numberOfDays / interval) + 1;
        for (int i = 0; i <= loop; ++i) {
            lfData.add(new LifeTableData());
            lfData.get(i).timeInterval = Double.parseDouble(String.valueOf(i * interval));
        }
    }

    private void computeN() {
        for (int k = 1; k < lfData.size(); ++k) {
            int n = data.size();
            for (Data datum : data) {
                if (datum.timeToFailure < lfData.get(k - 1).timeInterval) {
                    n--;
                } else
                    break;
            }
            lfData.get(k).n = n;
        }
    }

    private void computeD() {
        for (int k = 1; k < lfData.size(); ++k) {
            int d = 0;
            for (Data datum : data) {
                if ((datum.timeToFailure > lfData.get(k - 1).timeInterval) && (datum.timeToFailure <= lfData.get(k).timeInterval) && (datum.action.equals("F"))) {
                    d++;
                }
            }
            lfData.get(k).d = d;
        }
    }

    private void computeW() {
        for (int k = 1; k < lfData.size(); ++k) {
            int w = 0;
            for (Data datum : data) {
                if ((datum.timeToFailure > lfData.get(k - 1).timeInterval) && (datum.timeToFailure <= lfData.get(k).timeInterval) && (datum.action.equals("S"))) {
                    w++;
                }
            }
            lfData.get(k).w = w;
        }
    }

    private void computeSurvival() {
        lfData.get(0).survival = 1.0;
        for (int k = 1; k < lfData.size(); ++k) {
            double s = 1;
            LifeTableData l = lfData.get(k);
            s = lfData.get(k - 1).survival * (1.0 - (double) l.d / ((double) l.n - (double) l.w / 2.0));
            l.survival = s;
        }
    }

    private void computeMidpoint() {
        for (int i = 0; i < lfData.size()-1; i++) {
            LifeTableData lfDatum = lfData.get(i);
            lfDatum.midpoint = lfDatum.timeInterval + interval / 2.0;
        }
    }

    private void computeHazard() {
        for (int i = 1; i < lfData.size(); i++) {
            LifeTableData l = lfData.get(i);
            lfData.get(i - 1).hazard = l.survival * l.d / ((l.n - 0.5 * l.w) * interval);
        }
    }

    private void setTable() {
        for (LifeTableData datum : lfData) {
            HBox entry = new HBox();

            Label l;
            if (datum.timeInterval == null)
                l = new Label();
            else
                l = new Label(datum.timeInterval.toString());
            l.setPrefWidth(100);
            l.setMinWidth(100);
            l.setMaxWidth(100);
            entry.getChildren().add(l);

            if (datum.n == null)
                l = new Label();
            else
                l = new Label(datum.n.toString());
            l.setPrefWidth(37);
            l.setMinWidth(37);
            l.setMaxWidth(37);
            entry.getChildren().add(l);

            if (datum.d == null)
                l = new Label();
            else
                l = new Label(datum.d.toString());
            l.setPrefWidth(50);
            l.setMinWidth(50);
            l.setMaxWidth(50);
            entry.getChildren().add(l);

            if (datum.w == null)
                l = new Label();
            else
                l = new Label(datum.w.toString());
            l.setPrefWidth(30);
            l.setMinWidth(30);
            l.setMaxWidth(30);
            entry.getChildren().add(l);

            if (datum.survival == null)
                l = new Label();
            else
                l = new Label(datum.survival.toString());
            l.setPrefWidth(130);
            l.setMinWidth(130);
            l.setMaxWidth(130);
            entry.getChildren().add(l);

            if (datum.midpoint == null)
                l = new Label();
            else
                l = new Label(datum.midpoint.toString());
            l.setPrefWidth(100);
            l.setMinWidth(100);
            l.setMaxWidth(100);
            entry.getChildren().add(l);

            if (datum.hazard == null)
                l = new Label();
            else
                l = new Label(datum.hazard.toString());
            l.setMaxWidth(110);
            l.setMinWidth(110);
            l.setPrefWidth(110);
            entry.getChildren().add(l);

            entry.setSpacing(25);
            table.getChildren().add(entry);
        }
    }


    private void setLineChart() {
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Survival");
        for (LifeTableData datum : lfData) {
            series1.getData().add(new XYChart.Data<>(datum.timeInterval,datum.survival));
        }

        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Days elapsed");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Survival");


        final LineChart<Number, Number> sc = new LineChart<>(xAxis, yAxis);
        sc.getData().addAll(series1);

        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series2.setName("Hazard");
        for (int i = 0; i < lfData.size()-1; i++) {
            LifeTableData datum = lfData.get(i);
            series2.getData().add(new XYChart.Data<>(datum.midpoint, datum.hazard));
        }

        NumberAxis xAxis1 = new NumberAxis();
        xAxis1.setLabel("Days elapsed");

        NumberAxis yAxis1 = new NumberAxis();
        yAxis1.setLabel("Hazard");


        final LineChart<Number, Number> sc1 = new LineChart<>(xAxis1, yAxis1);
        sc1.getData().addAll(series2);
        charts.getChildren().addAll(sc,sc1);

    }
}
