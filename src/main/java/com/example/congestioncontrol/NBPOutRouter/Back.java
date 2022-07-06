package com.example.congestioncontrol.NBPOutRouter;

import com.example.congestioncontrol.Info.MessageCardModel;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class Back extends Thread
{
    String bef,ins,aft,all;
    Tim ti=new Tim();
    Back()
    {

    }

    Back(String str,int st,int nex,OutRouterView g)
    {

        try
        {
            Socket soc=new Socket("localhost",8888);
            bef=str.substring(0,st);
            ins="~localhost|"+ti.calculateTime();
            aft=str.substring(nex,str.length());
            System.out.println("connected Back: " + Thread.currentThread().getName());
            all=bef+ins+aft+"(Backward Feedback)";
            System.out.println("---------------------------------------------------------------\n");
            System.out.println(all+"\n");
            System.out.println("---------------------------------------------------------------"+"\n");
            g.displayMsg(new MessageCardModel(all));
            ObjectOutputStream oos=new ObjectOutputStream(soc.getOutputStream());
            oos.writeObject(all);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
