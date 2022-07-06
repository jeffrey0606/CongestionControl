package com.example.congestioncontrol.NBPRouter;

import com.example.congestioncontrol.Info.MessageCardModel;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Back extends Thread
{	RouterView rf;
    ServerSocket ss;
    Back(RouterView obj1)
    {
        rf=obj1;
        start();
    }

    public void run()
    {
        try
        {
            ss=new ServerSocket(8888);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        while(true)
        {
            try
            {
                Socket soc=ss.accept();
                ObjectInputStream ois=new ObjectInputStream(soc.getInputStream());
                String instring=(String) ois.readObject();
                System.out.println("---------------------------------------------------------------\n");
                System.out.println("---------------------------------------------------------------"+"\n");
                System.out.println(instring+"\n");
                rf.displayMsg(new MessageCardModel(instring));
                Socket soc1=new Socket("localhost",7777);
                ObjectOutputStream oos=new ObjectOutputStream(soc1.getOutputStream());
                oos.writeObject(instring);
                Thread.sleep(1000);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}