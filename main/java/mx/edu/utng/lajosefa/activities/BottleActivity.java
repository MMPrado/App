package mx.edu.utng.lajosefa.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

import mx.edu.utng.lajosefa.R;

public class BottleActivity extends AppCompatActivity {

    ImageView imvBottle;
    Button btnGirar;
    Random random;
    int agle;
    boolean restart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);

        random = new Random();
        imvBottle = findViewById(R.id.imv_bottle);
        btnGirar = findViewById(R.id.btn_turn);

        btnGirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(restart){
                    agle = agle % 360;
                    RotateAnimation rotate = new RotateAnimation(agle,360,
                            RotateAnimation.RELATIVE_TO_SELF,0.5f,
                            RotateAnimation.RELATIVE_TO_SELF,0.5f);
                    rotate.setFillAfter(true);
                    rotate.setDuration(360);
                    rotate.setInterpolator(new AccelerateDecelerateInterpolator());
                    imvBottle.startAnimation(rotate);
                    btnGirar.setText(R.string.go);
                    restart = false;
                }else{
                    agle = random.nextInt(3600) + 360;
                    RotateAnimation rotate = new RotateAnimation(0, agle, RotateAnimation.RELATIVE_TO_SELF,0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setFillAfter(true);
                    rotate.setDuration(3600);
                    rotate.setInterpolator(new AccelerateDecelerateInterpolator());
                    imvBottle.startAnimation(rotate);
                    restart = true;
                    btnGirar.setText(R.string.reset);
                }
            }
        });

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
