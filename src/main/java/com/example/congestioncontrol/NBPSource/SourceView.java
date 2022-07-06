package com.example.congestioncontrol.NBPSource;

import com.example.congestioncontrol.NBPOutRouter.Tim;
import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class SourceView implements Initializable {
    @FXML
    private JFXTextField ipTextField;
    @FXML
    private FontIcon ipIcon;

    @FXML
    private Label repeatMsg;

    @FXML
    private  Label times;

    @FXML
    private JFXSlider repeatSlider;

    @FXML
    private JFXButton sendBtn;

    @FXML
    private JFXTextArea msgTextArea;

    String dest_addr="";
    InetAddress in1;
    String Msg="";
    String conc="";
    String fin="";
    int st=0;
    int end=48;
    int len1=0;
    Socket soc;
    int split=0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ipTextField.setText("127.0.0.1");
//        msgTextArea.setText("Hello I'm Jeffrey Now Testing the Network Base Protocol Source (NBP).");

        repeatSlider.valueProperty().addListener((observableValue, old_val, new_val) -> {
            int repeatNum = new_val.intValue();

            if(repeatNum > 1) {
                times.setText("times");
                sendBtn.setDisable(false);
            } else {
                if(repeatNum == 0) {
                    sendBtn.setDisable(true);
                } else {
                    sendBtn.setDisable(false);
                }
                times.setText("time");
            }

            repeatMsg.setText(String.valueOf(repeatNum));
            System.out.println("old_val: " + old_val + " | new_val: " + new_val);
        });

        repeatSlider.setValue(1);
    }

    public void sendMsgs(ActionEvent actionEvent) {
        int repeat = Integer.parseInt(repeatMsg.getText());
        JFXSnackbar snackbar = new JFXSnackbar();
        if(repeat <= 0) {
            snackbar.show("Set the number of times a Msg will be repeated", "Click OK", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                }
            });
            return;
        }
        for (int i = 0; i < repeat; i++) {
            try
            {

                dest_addr = ipTextField.getText();
                System.out.println("**********************" + dest_addr + "****************");
                soc=new Socket("localhost",7788);



                SendPacket();
            }
            catch(IOException e1)
            {
                e1.printStackTrace();
                snackbar.show("InRouter Machine is Not Ready To Data Transfer", "Click OK", new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                    }
                });
            }
        }


    }

    public void clearFields(ActionEvent actionEvent) {
        repeatMsg.setText("");
        repeatSlider.setValue(0);
        ipTextField.setText("");
        msgTextArea.setText("");
    }

    public void  SendPacket() throws IOException
    {
        JFXSnackbar snackbar = new JFXSnackbar();

        try
        {
            in1 =InetAddress.getLocalHost();
            Msg = msgTextArea.getText();
            if(((dest_addr.trim()).length())>0)
            {
                if(((Msg.trim()).length())>0)
                {
                    OutputStream out  = soc.getOutputStream();
                    int st=0;
                    int end=48;
                    Tim ti=new Tim();
                    conc = dest_addr + "`" + in1.getHostName() + "`";
                    byte buffer[]=Msg.getBytes();
                    int len=buffer.length;
                    len1=len;
                    if(len<=48)
                    {
                        fin=conc+Msg + "\n" + "null";
                        byte buffer1[]=fin.getBytes();
                        out.write(buffer1);
                    }
                    else
                    {
                        fin=conc+Msg.substring(st,end)+"\n";
                        byte buffer2[]=fin.getBytes();
                        out.write(buffer2);
                        Thread.sleep(1000);
                        while(len1>48)
                        {
                            len1-=48;
                            if(len1<=48)
                            {
                                fin=conc+Msg.substring(end,len)+"\n"+"null";
                                byte buffer3[]=fin.getBytes();
                                out.write(buffer3);
                                Thread.sleep(1000);
                            }
                            else
                            {
                                split=end+48;
                                fin=conc+Msg.substring(end,split)+"\n";
                                end=split;
                                byte buffer5[]=fin.getBytes();
                                out.write(buffer5);
                                Thread.sleep(1000);
                            }
                        }
                    }
                    System.out.println("Message sent");
                }
                else
                {
                    System.out.println("There Is No Message To Send");
                    snackbar.show("There Is No Message To Send", "Click OK", new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {

                        }
                    });
                }
            }
            else
            {
                System.out.println("Destination Machine Name is Missing");
                snackbar.show("Destination Machine Name is Missing","Click OK", new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                    }
                });
            }
        }
        catch(UnknownHostException e)
        {
            System.out.println("Check the InRouter Machine Name");
            snackbar.show("Check the InRouter Machine Name","Click OK", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                }
            });
        }
        catch(InterruptedException e1)
        {
            System.out.println("Something wrong please try again");
            snackbar.show("Something wrong please try again","Click OK", new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                }
            });
        }
    }
}
