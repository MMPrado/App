package mx.edu.utng.lajosefa.activities.;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LinkActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnYouTube;
    private Button  btnFacebook, btnWhatsapp;




    private static String APP_DIRECTORY = "MyPictureApp/";
    private static String MEDIA_DIRECTORY = APP_DIRECTORY + "PictureApp";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnYouTube = (Button) findViewById(R.id.buttonYouTube);
        btnYouTube.setOnClickListener(this);

        btnFacebook = (Button) findViewById(R.id.buttonFacebook);
        btnFacebook.setOnClickListener(this);
        btnWhatsapp = (Button) findViewById(R.id.buttonwhatsapp);
        btnWhatsapp.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {


        btnYouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse(getString(R.string.youtube));
                Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(intent);
            }
        });


        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse(getString(R.string.facebook));
                Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(intent);
            }
        });

        btnWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(getString(R.string.text_plain));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.whatsapp));
                intent.setPackage(getString(R.string.paq_whatts));
                startActivity(intent);
            }
        });
    }


}

