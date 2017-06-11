package xyz.georgihristov.experiment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by gohv on 18.02.17.
 */

public class ChangeSettingsDialog extends DialogFragment {

    private EditText changeValueEditText;
    private CheckBox loggingCheckBox;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.dialog_layout,null);
        changeValueEditText = (EditText) dialogView.findViewById(R.id.changeGeneratedValueEditText);
        loggingCheckBox = (CheckBox) dialogView.findViewById(R.id.loggingCheckBox);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Settings")
                .setView(dialogView)
                .setMessage(R.string.choose_settings)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = changeValueEditText.getText().toString();
                        if(value.equals("")){
                            value = "10";
                        }else if(value.charAt(0) == '0'){
                            Toast.makeText(getActivity(), "Values cant be <= 0", Toast.LENGTH_SHORT).show();
                            value = "10";
                        }
                        if(loggingCheckBox.isChecked()){
                            MainActivity.generateLogging = true;
                        }else {
                            MainActivity.generateLogging = false;
                        }
                        MainActivity.generatedValue = (Integer.parseInt(value));
                        Log.d("TAG", String.valueOf(MainActivity.generateLogging));
                    }
                });
        AlertDialog dialog = builder.create();
        return dialog;
    }
}
