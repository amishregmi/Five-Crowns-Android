package aregmi.ramapo.edu.five_crowns.view;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import aregmi.ramapo.edu.five_crowns.R;

public class GameLog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_log);

        String file_name = "current_log.txt";
        String filenamewithpath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/saved_games/log/"+file_name;
        TextView text_view = findViewById(R.id.log_text_view);

        text_view.setMovementMethod(new ScrollingMovementMethod());

        File file = new File(filenamewithpath);
        StringBuilder text = new StringBuilder();

        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null){
                text.append(line);
                text.append("\n");
            }
            br.close();
        } catch (IOException ex){

        }

        String result = text.toString();
        text_view.setText(result);
    }
}
