package webfreak.si.srassessment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements VolumeControlInterface
{
    private VolumeControlView volumeControlView;
    TextView volumeLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        volumeControlView = findViewById(R.id.volume_control);
        volumeControlView.setVolumeControlScale(20);
        //volumeControlView.setVolumeControlColorFG(Color.RED);
        //volumeControlView.setVolumeControlLines(4);
        //volumeControlView.setVolumeControlVolume(100);

        Button setVolume = findViewById(R.id.set_volume);
        Button setLines = findViewById(R.id.set_lines);

        final EditText txtVolume = findViewById(R.id.txt_volume);
        final EditText txtLines = findViewById(R.id.txt_lines);

        volumeLabel = findViewById(R.id.volume_label);

        setVolume.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    volumeControlView.setVolumeControlVolume(Integer.parseInt(txtVolume.getText().toString().trim()));
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),"Not a valid number!",Toast.LENGTH_LONG).show();
                }
            }
        });

        setLines.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    volumeControlView.setVolumeControlLines(Utils.calculateLines(txtLines.getText().toString().trim(), volumeControlView.getVolumeControlScale()));
                }
                catch (Exception ex)
                {
                    Toast.makeText(getApplicationContext(),"Not a valid number!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onVolumeChanged(int volume)
    {
        volumeLabel.setText(getString(R.string.txt_volume_label).replace("{0}",String.valueOf(volume)));
    }
}
