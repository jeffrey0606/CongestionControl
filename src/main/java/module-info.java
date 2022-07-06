module com.example.conjestioncontrol {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.jfoenix;

    opens com.example.congestioncontrol to javafx.fxml;
    exports com.example.congestioncontrol;
    opens com.example.congestioncontrol.NBPSource to javafx.fxml;
    exports com.example.congestioncontrol.NBPSource;
    opens com.example.congestioncontrol.NBPInRouter to javafx.fxml;
    exports com.example.congestioncontrol.NBPInRouter;
    opens com.example.congestioncontrol.Info to javafx.fxml;
    exports com.example.congestioncontrol.Info;
    opens com.example.congestioncontrol.NBPRouter to javafx.fxml;
    exports com.example.congestioncontrol.NBPRouter;
    opens com.example.congestioncontrol.NBPOutRouter to javafx.fxml;
    exports com.example.congestioncontrol.NBPOutRouter;
}