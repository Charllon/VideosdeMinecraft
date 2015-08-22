package com.binaryfun.videosdeminecraft;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AboutUs  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        TextView email = (TextView) findViewById(R.id.Emails);
        TextView about = (TextView) findViewById(R.id.about);

        about.setText("O Vídeos de Minecraft é um aplicativo que reúne em um só lugar os melhores vídeos de minecraft do youtube. " +
                "\nNão somos responsáveis pelos conteúdos dos vídeos, apenas os indexamos.\n" +
                "\n" +
                "Sugestões, reclamações, elogios ou contato comercial:");
        email.setText("binaryfunmobile@gmail.com");

    }
}