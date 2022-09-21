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

import javax.swing.*;

import static sample.DbConn.getConnect;

public class Page2_1 {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button datesButton;

    @FXML
    private Button marksButton;

    @FXML
    private Button testsButton;

    @FXML
    void createPDFForDates(ActionEvent event) {

        try{
            String file_name = "C:\\Users\\Vlad\\Desktop\\жава эфх\\pdf-s\\File7_date_max_for_every.pdf";
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file_name));

            PdfPTable table = new PdfPTable(3);

            PdfPCell c1 = new PdfPCell (new Phrase("Name"));
            table.addCell(c1);

            c1 = new PdfPCell (new Phrase("Last Name"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Date"));
            table.addCell(c1);

            table.setHeaderRows(1);


            String post_select= "SELECT name, last, asks_date\n" +
                    "FROM askers A\n" +
                    "JOIN asks ON asks.askers_id = A.id \n" +
                    "WHERE asks_date >= ALL (SELECT asks_date\n" +
                    "\t\t\t\t\t\t\tFROM asks\n" +
                    "\t\t\t\t\t\t\tWHERE asks.askers_id = A.id)\n";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = prepSt.executeQuery();

            document.open();
            Paragraph para = new Paragraph("The latest date of creating ask for every asker");
            document.add(para);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            while(post_res.next()) {
                table.addCell(post_res.getString("name"));
                table.addCell(post_res.getString("last"));
                table.addCell(post_res.getString("asks_date"));
            }

            document.add(table);
            document.close();
        }catch (Exception e){
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "File error");
        }

    }

    @FXML
    void createPDFforStudents(ActionEvent event) {


        try{
            String file_name = "C:\\Users\\Vlad\\Desktop\\жава эфх\\pdf-s\\File9_union_stud_asker.pdf";
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file_name));

            PdfPTable table = new PdfPTable(4);

            PdfPCell c1 = new PdfPCell (new Phrase("Name"));
            table.addCell(c1);

            c1 = new PdfPCell (new Phrase("Last Name"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Count"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Text"));
            table.addCell(c1);

            table.setHeaderRows(1);


            String post_select= "SELECT last, name, COUNT(*), 'Counting tests which passed' AS text\n" +
                    "FROM students\n" +
                    "JOIN test_student ON students.id = test_student.students_id\n" +
                    "GROUP BY last, name\n" +
                    "UNION \n" +
                    "SELECT last, name, COUNT(*),'Counting asks which created' AS text\n" +
                    "FROM askers\n" +
                    "JOIN asks ON asks.askers_id = askers.id\n" +
                    "GROUP BY last, name\n";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = prepSt.executeQuery();

            document.open();
            Paragraph para = new Paragraph("Lists of students and askers with commentars");
            document.add(para);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            while(post_res.next()) {
                table.addCell(post_res.getString("name"));
                table.addCell(post_res.getString("last"));
                table.addCell(post_res.getString("count"));
                table.addCell(post_res.getString("text"));
            }

            document.add(table);
            document.close();
        }catch (Exception e){
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "File error");
        }

    }

    @FXML
    void createPDFforStuds(ActionEvent event) {



        try{
            String file_name = "C:\\Users\\Vlad\\Desktop\\жава эфх\\pdf-s\\File9_union_stud-s.pdf";
            Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(file_name));

            PdfPTable table = new PdfPTable(4);

            PdfPCell c1 = new PdfPCell (new Phrase("Name"));
            table.addCell(c1);

            c1 = new PdfPCell (new Phrase("Last Name"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Mark"));
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Text"));
            table.addCell(c1);

            table.setHeaderRows(1);


            String post_select= "SELECT name, last, MIN(mark) AS mark, 'With minimal mark ever got' AS text\n" +
                    "FROM students\n" +
                    "JOIN test_student ON students.id = test_student.students_id\n" +
                    "GROUP BY name, last\n" +
                    "UNION \n" +
                    "SELECT name, last, MAX(mark) AS mark, 'With maximal mark ever got' AS text\n" +
                    "FROM students\n" +
                    "JOIN test_student ON students.id = test_student.students_id\n" +
                    "GROUP BY name, last";
            PreparedStatement prepSt = getConnect().prepareStatement(post_select);

            ResultSet post_res = prepSt.executeQuery();

            document.open();
            Paragraph para = new Paragraph("Lists of students with min/max marks and comments");
            document.add(para);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            while(post_res.next()) {
                table.addCell(post_res.getString("name"));
                table.addCell(post_res.getString("last"));
                table.addCell(post_res.getString("mark"));
                table.addCell(post_res.getString("text"));
            }

            document.add(table);
            document.close();
        }catch (Exception e){
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "File error");
        }


    }

    @FXML
    void createTableForDates(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/DatesTablePDF.fxml"));
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

    @FXML
    void createTableForStudents(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/StudentsTableController.fxml"));
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

    @FXML
    void createTableForStuds(ActionEvent event) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/StudsTablePDF.fxml"));
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

    @FXML
    void initialize() {

    }

}

