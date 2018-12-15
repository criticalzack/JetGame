// CannonGame.java
// MainActivity displays the JetGameFragment
package com.deitel.cannongame;

import android.app.Activity;
import android.os.Bundle;

import org.json.JSONObject;

public class MainActivity extends Activity {

    //variable for highscores is declared here
    JSONObject[] highScores;
    // called when the app first launches
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // call super's onCreate method
        setContentView(R.layout.activity_main); // inflate the layout
        //downloadJson();
    }

    //this method can be called to download the Json object
    public void downloadJson()
    {
        //creates and executes an asynchronous task to download Json

        //the second parameter to the downloader is the class you're currently in, because the task needs a reference back
        //to be able to call the setJsonObjects method to set the results.
        //If you want to put the logic for downloading the Json in a different file, you'll need to modify the JsonDownloader class
        //slightly so that the constructor takes and initializes an object of the same type that you're currently in
        JsonDownloader downloader = new JsonDownloader(this, this);
        downloader.execute("http://craiginsdev.com/highscore/scores.php?game=MuppetsXtreme");
    }

    //this method is simply to be called by the asynchronous task to initialize the highScores object with the result that the task returns
    //otherwise, this method should probably not be called anywhere else
    public void setJsonObjects(JSONObject[] results)
    {
        highScores = new JSONObject[results.length];
        for(int i = 0; i < results.length; i++)
        {
            highScores[i] = results[i];
        }
    }
} // end class MainActivity

/*********************************************************************************
 * (C) Copyright 1992-2014 by Deitel & Associates, Inc. and * Pearson Education, *
 * Inc. All Rights Reserved. * * DISCLAIMER: The authors and publisher of this   *
 * book have used their * best efforts in preparing the book. These efforts      *
 * include the * development, research, and testing of the theories and programs *
 * * to determine their effectiveness. The authors and publisher make * no       *
 * warranty of any kind, expressed or implied, with regard to these * programs   *
 * or to the documentation contained in these books. The authors * and publisher *
 * shall not be liable in any event for incidental or * consequential damages in *
 * connection with, or arising out of, the * furnishing, performance, or use of  *
 * these programs.                                                               *
 *********************************************************************************/
