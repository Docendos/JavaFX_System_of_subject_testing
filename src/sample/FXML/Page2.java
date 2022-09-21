package sample.FXML;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.AddAskController;
import sample.AskController;

import javax.swing.*;

import static sample.DbConn.getConnect;

public class Page2 {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button asksButton;

    @FXML
    private Button marksButton;

    @FXML
    private Button testsButton;


    @FXML
    void createPDF(ActionEvent event) {
        try{
            String file_name = "C:\\Users\\Vlad\\Desktop\\жава эфх\\pdf-s\\File5.pdf";
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file_name));

            PdfPTable table = new PdfPTable(3);

            PdfPCell c1 = new PdfPCell (new Phrase("Name"));
            table.addCell(c1);

            c1 = new PdfPCell (new Phrase("Last Name"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Number asks"));
            table.addCell(c1);

            table.setHeaderRows(1);


            String post_select= "SELECT askers.name, askers.last, COUNT(*)\n" +
                    "FROM askers\n" +
                    "JOIN asks ON askers.id = asks.askers_id\n" +
                    "GROUP BY askers.name, askers.last\n";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = prepSt.executeQuery();

            document.open();
            Paragraph para = new Paragraph("How many asks were created by every author ");
            document.add(para);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            while(post_res.next()) {
                table.addCell(post_res.getString("name"));
                table.addCell(post_res.getString("last"));
                table.addCell(post_res.getString("count"));
            }

            document.add(table);
            document.close();
        }catch (Exception e){
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "File error");
        }
    }

    @FXML
    void initialize() {

    }

    public void createTableForAsks(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/AsksTablePDF.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Page2.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    public void createTableForTests(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/TestsTablePDF.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Page2.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    public void createPDFforTests(ActionEvent actionEvent) {

        try{
            String file_name = "C:\\Users\\Vlad\\Desktop\\жава эфх\\pdf-s\\File5_stud.pdf";
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file_name));

            PdfPTable table = new PdfPTable(2);

            PdfPCell c1 = new PdfPCell (new Phrase("Name"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Number tests"));
            table.addCell(c1);

            table.setHeaderRows(1);


            String post_select= "SELECT students.name, COUNT(*)\n" +
                    "FROM students\n" +
                    "JOIN test_student ON students.id = test_student.students_id\n" +
                    "GROUP BY students.name";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = prepSt.executeQuery();

            document.open();
            Paragraph para = new Paragraph("How many tests were passed by every student");
            document.add(para);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            while(post_res.next()) {
                table.addCell(post_res.getString("name"));
                table.addCell(post_res.getString("count"));
            }

            document.add(table);
            document.close();
        }catch (Exception e){
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "File error");
        }

    }

    public void createTableForMarks(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/MarksTablePDF.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(Page2.class.getName()).log(Level.SEVERE, null, ex);
        }
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    public void createPDFforAsks(ActionEvent actionEvent) {


        try{
            String file_name = "C:\\Users\\Vlad\\Desktop\\жава эфх\\pdf-s\\File7_stud_max_for_every.pdf";
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file_name));

            PdfPTable table = new PdfPTable(3);

            PdfPCell c1 = new PdfPCell (new Phrase("Name"));
            table.addCell(c1);

            c1 = new PdfPCell (new Phrase("Last Name"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("MAX Mark"));
            table.addCell(c1);

            table.setHeaderRows(1);


            String post_select= "SELECT name, last, mark\n" +
                    "FROM students S\n" +
                    "JOIN test_student ON S.id = test_student.students_id\n" +
                    "WHERE mark >= ALL (SELECT mark\n" +
                    "\t\t\t\t   FROM test_student\n" +
                    "\t\t\t\t   WHERE test_student.students_id = S.id)\n";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = prepSt.executeQuery();

            document.open();
            Paragraph para = new Paragraph("Max mark for every student");
            document.add(para);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            while(post_res.next()) {
                table.addCell(post_res.getString("name"));
                table.addCell(post_res.getString("last"));
                table.addCell(post_res.getString("mark"));
            }

            document.add(table);
            document.close();
        }catch (Exception e){
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "File error");
        }

    }
}
