package xyz.georgihristov.experiment;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Thread thread;
    private Timer timer;
    private List<BarEntry> entries;
    private BarChart barChart;
    private BarDataSet dataSet;
    private BarData lineData;
    private Random random;
    public static int generatedValue;
    public static boolean generateLogging = false;
    private boolean isStarted;
    private int rnd;
    private StartTask task;
    private MenuItem playItem;
    private MenuItem stopItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        textView = (TextView) findViewById(R.id.textView);
        barChart = (BarChart) findViewById(R.id.chart);
        barChart.setClickable(false);
        barChart.setNoDataText(getString(R.string.no_data_string));

        random = new Random();
        rnd = random.nextInt(10);
        generatedValue = 10;

        entries = new ArrayList<>();
    }
    private class StartTask extends AsyncTask<Integer, Integer, Integer> {
        int rnd;

        @Override
        protected Integer doInBackground(Integer... params) {
            thread = new Thread(
                    new Runnable() {
                        public void run() {
                            /*new timer*/
                            timer = new Timer();
                            timer.scheduleAtFixedRate(new TimerTask() {
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Random random = new Random();
                                            rnd = random.nextInt(generatedValue) + 1;
                                            entries.add(new BarEntry(rnd, rnd));
                                            dataSet = new BarDataSet(entries, getString(R.string.data_set));
                                            lineData = new BarData(dataSet);
                                            barChart.setData(lineData);
                                            barChart.setLogEnabled(generateLogging);
                                            barChart.invalidate();
                                            textView.setText(String.valueOf(rnd));
                                        }
                                    });
                                }
                            }, 0, 1000);
                        }
                    });
            thread.start();
            return rnd;
        }
    }

    private void createDialog() {
        ChangeSettingsDialog dialog = new ChangeSettingsDialog();
        dialog.show(getFragmentManager(), getString(R.string.fragment_show));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        playItem = menu.findItem(R.id.action_start);
        stopItem = menu.findItem(R.id.action_stop);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                createDialog();
                return true;

            case R.id.action_start:
                startTask();
                invalidateOptionsMenu();
                return true;

            case R.id.action_stop:
                isStarted = false;
                stopTask();
                invalidateOptionsMenu();
                return true;

            //if action was not recognized super class will handle it;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(isStarted){
            playItem.setVisible(false);
            stopItem.setVisible(true);
        }else if(!isStarted){
            playItem.setVisible(true);
            stopItem.setVisible(false);
        }
        return true;
    }
    private void startTask() {
        isStarted = true;
        task = new StartTask();
        task.execute(rnd);
        if(task == null){
            task = new StartTask();
            task.execute(rnd);
        }
    }
    private void stopTask() {
        task.cancel(true);
        timer.cancel();
        task.cancel(true);
        textView.setText(String.valueOf(task.getStatus()));
        barChart.clear();
        entries.clear();
    }
}
