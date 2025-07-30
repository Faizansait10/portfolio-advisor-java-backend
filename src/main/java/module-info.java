module com.example.portfolioproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.portfolioproject to javafx.fxml;
    exports com.example.portfolioproject;
}