module com.example.a2javaprogramming {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.a2javaprogramming to javafx.fxml;
    exports com.example.a2javaprogramming;
}