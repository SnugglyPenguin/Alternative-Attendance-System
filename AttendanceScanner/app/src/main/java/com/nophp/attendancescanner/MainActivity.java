package com.nophp.attendancescanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.snatik.storage.Storage;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity
{
    private File file;
    private FileInputStream inputStream;
    public String FILE_NAME = "roomid.txt";
    public BeepManager beep;
    public static android.support.v4.app.FragmentManager fragman;
    private DecoratedBarcodeView barcodeView = null;
    public static int delay = 1000;
    public long count = 0;

    CaptureManager capture;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String roomID = roomCheck();
        if (roomID == "")
        {
            roomSetup();
            roomID = roomCheck();
        }

        TextView textv = (TextView)findViewById(R.id.roomID);
        textv.setText(roomID);
        textv.setTextSize(48);
        textv.setTextColor(Color.BLACK);
        //textv.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.getViewFinder().setVisibility(View.VISIBLE);
        barcodeView.setStatusText("");

        CameraSettings cameraSettings = new CameraSettings();
        cameraSettings.setRequestedCameraId(1);
        barcodeView.getBarcodeView().setCameraSettings(cameraSettings);

        BarcodeCallback callback = new BarcodeCallback()
        {
            private long lastTime = 0;

            @Override
            public void barcodeResult(BarcodeResult result)
            {
                if (result.getText() != null)
                {
                    count = System.currentTimeMillis();
                    if(System.currentTimeMillis() - lastTime < delay)
                    {
                        return;
                    }
                    lastTime = System.currentTimeMillis();
                    scanner(result);
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints)
            {

            }
        };

        barcodeView.decodeContinuous(callback);

        //setContentView(barcodeView);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    protected void roomSetup()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Room ID");
        final EditText entry = new EditText(this);
        entry.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(entry);

        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String roomID = entry.getText().toString();
                try
                {
                    FileOutputStream fileOut = openFileOutput(FILE_NAME, MODE_APPEND);
                    fileOut.write(roomID.getBytes());
                    fileOut.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    protected String roomCheck() {
        String roomGet = "";
        file = new File(getFilesDir(), FILE_NAME);
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        try {
            inputStream = new FileInputStream(file);
            inputStream.read(bytes);
            inputStream.close();

            for (int i = 0; i < file.length(); i++) {

                String data = new String(bytes);
                roomGet = data.split("\\|")[i];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomGet;
    }
    protected void scanner(BarcodeResult result)
    {
        if (result != null)
        {
            if (result.getText() == null)
            {
                Toast.makeText(this, "Scanning Cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {
                String text = "http://51.68.136.156/rwp/scanner.php?qr=" + result.getText();
                String textReturn = readWeb(text.toLowerCase());
                String errorCheckedText = errorCheck(textReturn);
                if (errorCheckedText == "Success!")
                {
                    MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beep);
                    mediaPlayer.start();
                    TextView textv = (TextView)findViewById(R.id.textView);
                    textv.setText("You arrived on time!");
                    textv.setTextSize(48);
                    textv.setTextColor(Color.WHITE);
                    textv.setBackgroundColor(Color.GREEN);
                    textv.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
                }
                else if (errorCheckedText == "Late!")
                {
                    MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beep);
                    mediaPlayer.start();
                    TextView textv = (TextView)findViewById(R.id.textView);
                    textv.setText("You are late!");
                    textv.setTextSize(48);
                    textv.setTextColor(Color.BLACK);
                    textv.setBackgroundColor(Color.YELLOW);
                    textv.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
                }
                else
                {
                    MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.beep2);
                    mediaPlayer.start();
                    mediaPlayer.start();
                    TextView textv = (TextView)findViewById(R.id.textView);
                    textv.setText(errorCheckedText);
                    textv.setTextSize(48);
                    textv.setTextColor(Color.WHITE);
                    textv.setBackgroundColor(Color.RED);
                    textv.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);

                }
               // Toast.makeText(this, errorCheckedText, Toast.LENGTH_LONG).show();
            }
        }
    }

    public String errorCheck(String hashed)
    {

        String segments[] = hashed.split("\"");

        String text = segments[segments.length - 3];
        System.out.println(text);

        String toastMessage = "?";

        if (text.contains("0"))
        {
            toastMessage = "Success!";
        }

        else if (text.contains("1"))
        {
            toastMessage = "Error 1: Incorrect hash";
        }

        else if (text.contains("2"))
        {
            toastMessage = "Error 2: Scanner ID is not an integer";
        }

        else if (text.contains("3"))
        {
            toastMessage = "Error 3: Issue reaching database, try again!";

            String currentTimeDateString = DateFormat.getDateTimeInstance().format(new Date());//gets the current time and date

            Storage storage = new Storage(getApplicationContext());//creates storage
            String path = storage.getInternalFilesDirectory();//used to get internal storage over SD card/ external storage
            System.out.println(path);//path of file is printed in the terminal
            storage.createFile(path + "/errorMessageFile.txt", currentTimeDateString + ": Error 3: Issue reaching database, try again!");//file is created along with the message
            String finalPath = path + "/errorMessageFile.txt";//make the storage file a string
            String content = storage.readTextFile(finalPath);//^^
            System.out.println("Some text " + content);//test the string is working as planned

        }

        else if (text.contains("4"))
        {
            toastMessage = "Error 4: QR code does not match";
        }

        else if (text.contains("5"))
        {
            toastMessage = "Error 5: QR code expired";
        }
        else if (text == "FAIL")
        {
            toastMessage = "Error A1: Failure to establish internet connection";
        }

        return toastMessage;
    }

    public String readWeb(String text)
    {
        try (Scanner scanner = new Scanner(new URL(text).openStream(), StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
        catch (MalformedURLException e)
        {
            System.out.println("Malformed URL: " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("I/O Error: " + e.getMessage());
        }
        return "FAIL";
    }

}
