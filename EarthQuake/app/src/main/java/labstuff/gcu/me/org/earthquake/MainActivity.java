package labstuff.gcu.me.org.earthquake;

/*  Starter project for Mobile Platform Development in Semester B Session 2018/2019
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 ________________
// Student ID           _________________
// Programme of Study   _________________
//

// Update the package name to include your Student Identifier

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import labstuff.gcu.me.org.earthquake.R;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private TextView rawDataDisplay;
    private Button startButton;
    private String result;
    private String urll="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);
        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);

        // More Code goes here
    }

    public void onClick(View aview)
    {
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                //
                boolean data = false;
                while ((inputLine = in.readLine()) != null)
                {
                    if(inputLine.contains("item")){
                        data = true;
                    }
                    if(data == true) {
                        if (inputLine.contains("description")) {
                            result = result + inputLine + "\n";
                            Log.e("MyTag",inputLine);
                        }
                    }

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it
            //

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    rawDataDisplay.setText(result);
                }
            });
        }

    }

}
